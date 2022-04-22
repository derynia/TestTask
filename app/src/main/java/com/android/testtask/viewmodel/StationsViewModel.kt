package com.android.testtask.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.repo.StationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val stationsRepo: StationsRepo,
    private val externalScope: CoroutineScope = GlobalScope
    ) : ViewModel() {

    val stations : MediatorLiveData<List<Stations>> by lazy {
        MediatorLiveData<List<Stations>>()
    }

    fun getStations() = stations.addSource(
        stationsRepo.stationsAsLiveData()
    ) {
        stations.value = it
    }

    fun delete(station: Stations) {
        externalScope.launch {
            station.deleted = true
            station.synced = false
            stationsRepo.update(station)
        }
    }
}