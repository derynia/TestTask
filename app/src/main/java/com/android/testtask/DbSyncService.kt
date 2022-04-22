package com.android.testtask

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.repo.StationsRepo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class DbSyncService : Service() {
    val TAG = "DBSyncService"
    @Inject lateinit var stationsRepo: StationsRepo
    val fireDB = Firebase.firestore

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun deleteRecord(station: Stations) {
        fireDB.collection(COLLECTION)
            .document(station.id.toString())
            .delete()
            .addOnSuccessListener {
                GlobalScope.launch(Dispatchers.IO) {
                    stationsRepo.delete(station)
                }
            }
            .addOnFailureListener { e ->  Log.d(ERROR_TAG, "${station.id} - ${e.message.toString()}") }
    }

    private suspend fun insertUpdate(station: Stations) {
        val record = hashMapOf(
            "fuelType" to station.fuelType.toString(),
            "qty" to station.qty,
            "sum" to station.sum,
            "latitude" to station.latitude,
            "longitude" to station.longitude,
            "address" to station.address
        )

        fireDB.collection(COLLECTION)
            .document(station.id.toString())
            .set(record)
            .addOnSuccessListener {
                station.synced = true
                GlobalScope.launch(Dispatchers.IO) {
                    stationsRepo.update(station)
                }
            }
            .addOnFailureListener { e ->  Log.d(ERROR_TAG, "${station.id} - ${e.message.toString()}") }
    }

    private suspend fun syncStation(station: Stations) {
        when (station.deleted) {
            true -> {
                deleteRecord(station)
            }
            else -> {
                insertUpdate(station)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            return START_STICKY
        }

        isRunning = true
        Thread {
            var syncList : List<Stations> = stationsRepo.stationsUnsynced()

            while (syncList.isNotEmpty()) {
                syncList.forEach {
                    runBlocking {
                        syncStation(it)
                    }
                }
                syncList = stationsRepo.stationsUnsynced()

                if (syncList.isNotEmpty()) {
                    Thread.sleep(5000)
                }
            }

            isRunning = false
            stopSelf()
            Log.d("DataSync", "Service stopped")
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    companion object {
        var isRunning = false
        const val COLLECTION = "stations"
        const val ERROR_TAG = "SyncError"
    }
}