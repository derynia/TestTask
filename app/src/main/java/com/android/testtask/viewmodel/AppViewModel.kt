package com.android.testtask.viewmodel

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.android.testtask.db.MainDB
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.repo.StationsRepo
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

//@HiltViewModel
//class AppViewModel() (lateinit var stat) : AndroidViewModel(app) {
//    lateinit var stationsRepo: StationsRepo
//
//    val stations : MediatorLiveData<List<Stations>> by lazy {
//        MediatorLiveData<List<Stations>>()
//    }
//
//    fun getStations() = stations.addSource(
//        stationsRepo.stationsUnsyncedAsLiveData()
//    ) {
//        stations.value = it
//    }
//}