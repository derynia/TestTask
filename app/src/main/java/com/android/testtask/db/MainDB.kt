package com.android.testtask.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.testtask.db.dao.StationsDao
import com.android.testtask.db.entity.Stations

@Database(entities = [Stations::class], version = 2)
abstract class MainDB : RoomDatabase() {
    internal abstract fun stationsDao(): StationsDao

    companion object {
        fun getInstance(context: Context): MainDB =
            Room.databaseBuilder(context, MainDB::class.java, "Test Task DB")
                .fallbackToDestructiveMigration()
                .build()
    }
}