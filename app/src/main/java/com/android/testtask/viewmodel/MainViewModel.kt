package com.android.testtask.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.repo.StationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stationsRepo: StationsRepo
) : ViewModel() {
    val stations : MediatorLiveData<List<Stations>> by lazy {
        MediatorLiveData<List<Stations>>()
    }

    fun getStations() = stations.addSource(
        stationsRepo.stationsUnsyncedAsLiveData()
    ) {
        stations.value = it
    }
}