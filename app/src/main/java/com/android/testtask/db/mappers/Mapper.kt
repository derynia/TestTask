package com.android.testtask.db.mappers

interface Mapper<T, E> {
    fun mapToLocal(value: T) : E
}