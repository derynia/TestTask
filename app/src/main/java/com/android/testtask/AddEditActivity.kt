package com.android.testtask

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.testtask.databinding.ActivityAddEditBinding
import com.android.testtask.db.FuelType
import com.android.testtask.db.entity.Stations
import com.android.testtask.utils.showError
import com.android.testtask.viewmodel.AddEditViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.concurrent.TimeUnit

private lateinit var binding: ActivityAddEditBinding
@AndroidEntryPoint
class AddEditActivity : AppCompatActivity(), OnMapReadyCallback {
    val viewModel: AddEditViewModel by viewModels()
    private lateinit var spinnerAdapter : ArrayAdapter<FuelType>
    private var currentId: Long = -1
    private var currentLatitude : Double = 0.0
    private var currentLongitude : Double= 0.0
    private var currentAddressStr : String = ""
    private lateinit var map: GoogleMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation : LocationResult
    private val mark = MarkerOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    @SuppressLint("MissingPermission")
    private fun setupFusedLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(UPDATE_TIME)
            fastestInterval = TimeUnit.SECONDS.toMillis(MIN_UPDATE_TIME)
            maxWaitTime = TimeUnit.MINUTES.toMillis(MAX_WAIT_TIME)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lat = locationResult.lastLocation.latitude
                val long =locationResult.lastLocation.longitude
                if (!::currentLocation.isInitialized
                    || currentLocation.lastLocation.latitude != lat
                    || currentLocation.lastLocation.longitude != long) {
                    val cPosition = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                    mark
                        .position(cPosition)
                        .title("Address")

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(cPosition, ZOOM_LEVEL))
                    map.addMarker(mark)
                    currentLocation = locationResult
                }
            }
        }

        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                it
            )
        }
    }


    private fun bindStationData() {
        if (viewModel.isEdit()) {
            with (viewModel.stationLive.value) {
                binding.spinnerFuelType.setSelection(spinnerAdapter.getPosition(this?.fuelType))
                binding.editFieldSupplier.setText(this?.supplier)
                this?.qty?.let { binding.editFieldQty.setText(it.toString()) }
                this?.sum?.let { binding.editFieldSum.setText(it.toString()) }
                currentLongitude = this?.longitude ?: 0.0
                currentLatitude = this?.latitude ?: 0.0
                currentAddressStr = this?.address ?: ""
                binding.textAddress.text = currentAddressStr
            }
        }
        setupMap()
    }

    private fun getStationMap() =
        mapOf(
            "supplier" to binding.editFieldSupplier.text.toString(),
            "fuelType" to binding.spinnerFuelType.selectedItem,
            "qty" to binding.editFieldQty.text.toString(),
            "sum" to binding.editFieldSum.text.toString(),
            "latitude" to currentLatitude,
            "longitude" to currentLongitude,
            "address" to currentAddressStr
        )

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapMain) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun setupObserver() {
        val stationObserver = Observer<Stations> {
            bindStationData()
        }

        intent.extras.let {
            currentId = it?.getLong("ID") ?: -1
        }

        viewModel.stationLive.observe(this, stationObserver)
        viewModel.initStation(currentId)
    }

    private fun setButtonClickers() {
        binding.buttonSubmit.setOnClickListener {
            val res = viewModel.validateAndSaveStation(getStationMap())
            when {
                res.isBlank() -> this.onBackPressed()
                else -> showError(this, getString(R.string.station_save_error), res)
            }
        }

        binding.buttonCancel.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun setupView() {
        spinnerAdapter = ArrayAdapter<FuelType>(this, android.R.layout.simple_spinner_item, FuelType.values())
        binding.spinnerFuelType.adapter = spinnerAdapter

        setupObserver()
        binding.textHeader.text = getString(
            when (currentId) {
                -1L -> R.string.add_station
                else -> R.string.edit_station
            }
        )

        setButtonClickers()
    }

    private fun mapOnClick(point: LatLng) {
        currentLatitude = point.latitude
        currentLongitude = point.longitude
        currentAddressStr = ""

        val geocoder = Geocoder(applicationContext)
        var addresses: List<Address?> = ArrayList()
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1)
        } catch (e: IOException) {
            showError(this@AddEditActivity, getString(R.string.maps_error), e.message.toString())
        }

        if (addresses.isEmpty()) {
//            showError(this, getString(R.string.maps_error), getString(R.string.maps_read_error))
            return
        }

        val address: Address? = addresses[0]

        if (address != null) {
            val sb = StringBuilder()
            for (i in 0..address.maxAddressLineIndex) {
                sb.append(address.getAddressLine(i) + "\n")
            }
            currentAddressStr = sb.toString()
            binding.textAddress.text = currentAddressStr
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        val cPosition = LatLng(currentLatitude, currentLongitude)
        map = googleMap

        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener { point -> mapOnClick(point) }

        mark
            .position(cPosition)
            .title("Address")

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cPosition, ZOOM_LEVEL))
        map.addMarker(mark)
        setupFusedLocation()
    }

    companion object {
        const val ZOOM_LEVEL = 13F
        const val UPDATE_TIME = 60L
        const val MIN_UPDATE_TIME = 30L
        const val MAX_WAIT_TIME = 2L
    }

    override fun onStop() {
        super.onStop()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}