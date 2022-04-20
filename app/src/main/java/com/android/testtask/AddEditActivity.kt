package com.android.testtask

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
import dagger.hilt.android.AndroidEntryPoint

private lateinit var binding: ActivityAddEditBinding
@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {
    val viewModel: AddEditViewModel by viewModels()
    private lateinit var spinnerAdapter : ArrayAdapter<FuelType>
    private var currentId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun bindStationData() {
        if (viewModel.isEdit()) {
            with (viewModel.station.value) {
                binding.spinnerFuelType.setSelection(spinnerAdapter.getPosition(this?.fuelType))
                binding.editFieldSupplier.setText(this?.supplier)
                this?.qty?.let { binding.editFieldQty.setText(it.toString()) }
                this?.sum?.let { binding.editFieldSum.setText(it.toString()) }
            }
        }
    }

    private fun getStation(stationId: Long) = Stations(
            stationId,
            binding.editFieldSupplier.text.toString(),
            binding.spinnerFuelType.selectedItem as FuelType,
            binding.editFieldQty.text.toString().toInt(),
            binding.editFieldSum.text.toString().toDouble(),
            false
        )

    private fun setupView() {
        spinnerAdapter = ArrayAdapter<FuelType>(this, android.R.layout.simple_spinner_item, FuelType.values())
        binding.spinnerFuelType.adapter = spinnerAdapter

        val stationObserver = Observer<Stations> { newStation ->
            bindStationData()
        }

        intent.extras.let {
            currentId = it?.getLong("ID") ?: -1
        }

        viewModel.station.observe(this, stationObserver)
        viewModel.initStation(currentId)

        binding.textHeader.text = getString(
            when (currentId) {
                -1L -> R.string.add_station
                else -> R.string.edit_station
            }
        )

        binding.buttonSubmit.setOnClickListener {
            val stationId = viewModel.station.value?.id ?: 0
            val res = viewModel.validateAndSaveStation(getStation(stationId))
            when {
                res.isBlank() -> this.onBackPressed()
                else ->  showError(this, STATION_SAVE_ERROR, res)
            }
        }

        binding.buttonCancel.setOnClickListener {
            this.onBackPressed()
        }
    }

}