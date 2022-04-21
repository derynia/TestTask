package com.android.testtask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.testtask.R
import com.android.testtask.db.FuelType
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.repo.StationsRepo
import com.android.testtask.di.AppModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val stationsRepo: StationsRepo,
    private val externalScope: CoroutineScope = GlobalScope,
    private val resourcesProvider: AppModule.ResourcesProvider
) : ViewModel() {
    private var isEditValue = false
    var station: Stations = Stations()
    val stationLive : MutableLiveData<Stations> by lazy {
        MutableLiveData<Stations>()
    }

    fun isEdit() = isEditValue

    fun initStation(stationId : Long) {
        if (stationId == -1L) {
            stationLive.postValue(station)
        } else {
            isEditValue = true
            viewModelScope.launch(Dispatchers.IO) {
                station = stationsRepo.stationById(stationId)
                stationLive.postValue(station)
            }
        }
    }

    private fun fillStationFields(map: Map<String, Any?>) {
        station.supplier = map["supplier"] as String
        station.fuelType = map["fuelType"] as FuelType
        station.qty = (map["qty"] as String).toInt()
        station.sum = (map["sum"] as String).toDouble()
        station.latitude = map["latitude"] as Double
        station.longitude = map["longitude"] as Double
        station.address = map["address"] as String
        station.synced = false
    }

    fun validateAndSaveStation(map: Map<String, Any?>): String {
        fillStationFields(map)

        return when {
            station.supplier.isBlank() -> resourcesProvider.getString(R.string.supplier_required)
            station.qty == 0 -> resourcesProvider.getString(R.string.qty_required)
            station.sum == 0.0 -> resourcesProvider.getString(R.string.sum_required)
            else -> {
                externalScope.launch {
                    stationsRepo.insert(station)
                }

                ""
            }
        }
    }
}
