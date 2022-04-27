package com.android.testtask

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.testtask.db.entity.Stations
import com.android.testtask.db.mappers.StationMapper
import com.android.testtask.db.repo.StationsRepo
import com.google.firebase.firestore.FieldPath
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
    private val fireDB = Firebase.firestore

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
            "supplier" to station.supplier,
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

    private fun readDataFromFS(listNotLoading: ArrayList<String>) : Boolean {
        var isNotSucess = false
        if (listNotLoading.size == 0) {
            listNotLoading.add("id")
        }

        fireDB.collection(COLLECTION)
            .whereNotIn(FieldPath.documentId(), listNotLoading)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Docs", documents.size().toString())
                for (document in documents) {
                    GlobalScope.launch(Dispatchers.IO) {
                        stationsRepo.insert(StationMapper().mapToLocal(document))
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.d(ERROR_TAG, e.message.toString())
                isNotSucess = true
            }

        return isNotSucess
    }

    private fun syncDb() {
        Thread {
            var syncList: List<Stations> = stationsRepo.stationsUnsynced()

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

            val stationsIDs = ArrayList<String>()
            stationsRepo.stationsList().forEach {
                stationsIDs.add(it.id.toString())
            }

            var presentDataFromFS = true
            while (presentDataFromFS) {
                runBlocking {
                    presentDataFromFS = readDataFromFS(stationsIDs)
                }

                if (presentDataFromFS) {
                    Thread.sleep(5000)
                }

            }

            isRunning = false
            stopSelf()
            Log.d("DataSync", "Service stopped")
        }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            return START_STICKY
        }

        isRunning = true
        syncDb()
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