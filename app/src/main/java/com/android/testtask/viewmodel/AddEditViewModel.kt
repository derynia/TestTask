package com.android.testtask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.testtask.R
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
    val station : MutableLiveData<Stations> by lazy {
        MutableLiveData<Stations>()
    }

    fun isEdit() = isEditValue

    fun initStation(stationId : Long) {
        if (stationId == -1L) {
            station.postValue(Stations())
        } else {
            isEditValue = true
            viewModelScope.launch(Dispatchers.IO) {
                station.postValue(stationsRepo.stationById(stationId))
            }
        }
    }

    fun validateAndSaveStation(stationToSave: Stations): String =
        when {
            stationToSave.supplier.isBlank() -> resourcesProvider.getString(R.string.supplier_required)
            stationToSave.qty == 0 -> resourcesProvider.getString(R.string.qty_required)
            stationToSave.sum == 0.0 -> resourcesProvider.getString(R.string.sum_required)
            else -> {
                externalScope.launch {
                    stationsRepo.insert(stationToSave)
                }

                ""
            }
        }
}