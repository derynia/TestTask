package com.android.testtask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.repo.StationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val stationsRepo: StationsRepo
    ) : ViewModel() {

    val stations : MutableLiveData<List<Stations>> by lazy {
        MutableLiveData<List<Stations>>()
    }

    fun getStations() = viewModelScope.launch(Dispatchers.IO) {
        stations.postValue(stationsRepo.stationsList())
    }
}