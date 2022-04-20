package com.android.testtask

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.testtask.databinding.ActivityAddEditBinding
import com.android.testtask.db.FuelType
import com.android.testtask.db.entity.Stations
import com.android.testtask.utils.STATION_SAVE_ERROR
import com.android.testtask.utils.showError
import com.android.testtask.viewmodel.AddEditViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


private lateinit var binding: ActivityAddEditBinding
@AndroidEntryPoint
class AddEditActivity : AppCompatActivity(), OnMapReadyCallback {
    val viewModel: AddEditViewModel by viewModels()
    private lateinit var spinnerAdapter : ArrayAdapter<FuelType>
    private var currentId: Long = -1
    private var latitude : Double = 0.0
    private var longitude : Double= 0.0
    private var addressStr : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun bindStationData() {
        if (viewModel.isEdit()) {
            with (viewModel.stationLive.value) {
                binding.spinnerFuelType.setSelection(spinnerAdapter.getPosition(this?.fuelType))
                binding.editFieldSupplier.setText(this?.supplier)
                this?.qty?.let { binding.editFieldQty.setText(it.toString()) }
                this?.sum?.let { binding.editFieldSum.setText(it.toString()) }
                this?.longitude?.let { longitude }
                this?.latitude?.let { longitude }
                this?.address?.let { address }
            }
        }
    }

    private fun getStationMap() =
        mapOf(
            "supplier" to binding.editFieldSupplier.text.toString(),
            "fuelType" to binding.spinnerFuelType.selectedItem,
            "qty" to binding.editFieldQty.text.toString(),
            "sum" to binding.editFieldSum.text.toString(),
            "latitude" to latitude,
            "longitude" to longitude,
            "address" to addressStr
        )

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapMain) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun setupObserver() {
        val stationObserver = Observer<Stations> { _ ->
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
                else -> showError(this, STATION_SAVE_ERROR, res)
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
        setupMap()
    }

    fun mapOnClick(point: LatLng) {
        latitude = point.latitude
        longitude = point.longitude
        addressStr = ""

        val geocoder = Geocoder(applicationContext)
        var addresses: List<Address?> = ArrayList()
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1)
        } catch (e: IOException) {
            showError(this@AddEditActivity, "Ooops", e.message.toString())
        }

        val address: Address? = addresses[0]

        if (address != null) {
            val sb = StringBuilder()
            for (i in 0..address.maxAddressLineIndex) {
                sb.append(address.getAddressLine(i) + "\n")
            }
            addressStr = sb.toString()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title("Address")
        )

        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.setOnMapClickListener { point -> mapOnClick(point) }
    }
}