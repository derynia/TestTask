package com.android.testtask.db.mappers

import com.android.testtask.db.FuelType
import com.android.testtask.db.entity.Stations
import com.google.firebase.firestore.QueryDocumentSnapshot

class StationMapper : Mapper<QueryDocumentSnapshot, Stations> {
    private fun getByName(fuelName: String) : FuelType =
        when (fuelName) {
            "Petrol" -> FuelType.PETROL
            "Diesel" -> FuelType.DIESEL
            "Electric" -> FuelType.ELECTRIC
            "Gas" -> FuelType.GAS
            else -> FuelType.PETROL
        }

    override fun mapToLocal(value: QueryDocumentSnapshot): Stations  =
        Stations(
            id = value.id.toLong(),
            supplier = value.get("supplier").toString(),
            fuelType = getByName(value.get("fuelType") as String),
            qty = (value.get("qty") as Long).toInt(),
            sum = value.get("sum") as Double,
            latitude = value.get("latitude") as Double,
            longitude = value.get("longitude") as Double,
            address = value.get("address") as String,
            synced = true,
            deleted = false
        )
}