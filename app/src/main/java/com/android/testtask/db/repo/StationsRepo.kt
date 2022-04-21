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

    fun stationsAsLiveData() : LiveData<List<Stations>> = stationsDao.stationsAsLiveData()

    suspend fun delete(station: Stations) = stationsDao.delete(station)

    suspend fun deleteList(stations: List<Stations>) = stationsDao.deleteList(stations)

    suspend fun update(station: Stations) = stationsDao.update(station)

    suspend fun updateList(stations: List<Stations>) = stationsDao.updateList(stations)
}