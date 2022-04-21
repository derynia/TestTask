package com.android.testtask.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.testtask.db.entity.Stations
import kotlinx.coroutines.flow.Flow

@Dao
interface StationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: Stations)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(stationsList: List<Stations>)

    @Update
    suspend fun update(station: Stations)

    @Update
    suspend fun updateList(stationsList: List<Stations>)

    @Delete
    suspend fun delete(station: Stations)

    @Delete
    suspend fun deleteList(stationsList: List<Stations>)

    @Query("SELECT * FROM Stations")
    fun fullStationsList() : List<Stations>

    @Query("SELECT * FROM Stations WHERE deleted = 0")
    fun stationsList() : List<Stations>

    @Query("SELECT * FROM Stations WHERE deleted = 0")
    fun stationsAsLiveData() : LiveData<List<Stations>>

    @Query("SELECT * FROM Stations WHERE Stations.id = :id")
    fun getById(id: Long) : Stations
}