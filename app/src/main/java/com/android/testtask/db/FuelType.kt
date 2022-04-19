package com.android.testtask.db

enum class FuelType(val fType: Byte) {
    PETROL(0) {
        override fun toString(): String {
            return "Petrol"
        }
    },
    DIESEL(1) {
        override fun toString(): String {
            return "Diesel"
        }
    },
    ELECTRIC(2) {
        override fun toString(): String {
            return "Electric"
        }
    },
    GAS(3) {
        override fun toString(): String {
            return "Gas"
        }
    }
}