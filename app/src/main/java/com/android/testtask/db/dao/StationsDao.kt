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

    @Query("SELECT * FROM Stations")
    fun stationsList() : List<Stations>

    @Query("SELECT * FROM Stations")
    fun stationsAsFlow() : Flow<List<Stations>>

    @Query("SELECT * FROM Stations WHERE Stations.id = :id")
    fun getById(id: Long) : Stations
}