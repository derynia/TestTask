package com.android.testtask.db.repo

import androidx.lifecycle.LiveData
import com.android.testtask.db.MainDB
import com.android.testtask.db.entity.Stations
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StationsRepo @Inject constructor(
    database: MainDB
)  {
    private val stationsDao = database.stationsDao()

    suspend fun insert(station: Stations) = stationsDao.insert(station)

    suspend fun insertList(stations: List<Stations>) =  stationsDao.insertList(stations)

    fun stationsList() : List<Stations> = stationsDao.stationsList()

    fun stationById(id: Long) : Stations = stationsDao.getById(id)

    fun stationsAsFlow() : Flow<List<Stations>> = stationsDao.stationsAsFlow()
}