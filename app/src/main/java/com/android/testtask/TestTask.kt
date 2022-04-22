package com.android.testtask

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.testtask.db.MainDB
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.repo.StationsRepo
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.internal.Contexts.getApplication

@HiltAndroidApp
class TestTask : Application()
//{
////    private lateinit var dbViewModel : AppViewModel
//    private lateinit var stationsRepo: StationsRepo
//    private lateinit var stationsObserver: Observer<List<Stations>>
//
//    val stations : MediatorLiveData<List<Stations>> by lazy {
//        MediatorLiveData<List<Stations>>()
//    }
//
//    private fun getStations() = stations.addSource(
//        stationsRepo.stationsUnsyncedAsLiveData()
//    ) {
//        stations.value = it
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        stationsRepo = StationsRepo(MainDB.getInstance(this.applicationContext))
//        stationsObserver = Observer<List<Stations>> { newStation ->
//            Log.d("DataChange", "Live data changed " + newStation.size)
//            Toast.makeText(this, "Data changed", Toast.LENGTH_LONG).show()
//        }
//
////        dbViewModel =
////            ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication(this))
////                .create(AppViewModel::class.java)
//
////        val stationsObserver = Observer<List<Stations>> { newStation ->
////            Toast.makeText(this, "Data changed", Toast.LENGTH_LONG).show()
////        }
//
//        stations.observeForever(stationsObserver)
//        getStations()
//    }
//
//    override fun onTerminate() {
//        super.onTerminate()
//        stations.removeObserver(stationsObserver)
//    }
//}
//
