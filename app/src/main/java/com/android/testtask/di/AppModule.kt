package com.android.testtask.di

import android.content.Context
import androidx.annotation.StringRes
import com.android.testtask.db.MainDB
import com.android.testtask.db.repo.StationsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
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

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Singleton
    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        val dispatcherProvider : DispatcherProvider = provideDispatchers()
        return CoroutineScope(SupervisorJob() + dispatcherProvider.io)
    }

    @Singleton
    class ResourcesProvider @Inject constructor(
        @ApplicationContext private val context: Context
    ) {
        fun getString(@StringRes stringResId: Int): String {
            return context.getString(stringResId)
        }
    }

}