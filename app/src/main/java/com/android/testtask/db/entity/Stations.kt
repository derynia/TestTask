package com.android.testtask.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.testtask.db.FuelType

@Entity(tableName = "Stations")
data class Stations(
    @PrimaryKey(autoGenerate = true) @ColumnInfo val id: Long = 0,
    @ColumnInfo(name = "supplier") var supplier: String = "",
    @ColumnInfo(name = "fuelType") var fuelType: FuelType = FuelType.PETROL,
    @ColumnInfo(name = "qty") var qty: Int = 0,
    @ColumnInfo(name = "sum") var sum: Double = 0.0,
    @ColumnInfo(name = "latitude") var latitude: Double = 0.0,
    @ColumnInfo(name = "longitude") var longitude: Double = 0.0,
    @ColumnInfo(name = "address") var address: String = "",
    @ColumnInfo(name = "synced") var synced: Boolean = false
)
