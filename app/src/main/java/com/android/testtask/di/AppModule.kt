package com.android.testtask.di

import android.content.Context
import com.android.testtask.db.MainDB
import com.android.testtask.db.repo.StationsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): MainDB {
        return MainDB.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideStationsRepository(mainDB: MainDB) = StationsRepo(mainDB)
}