package com.example.satellite.di

import android.content.Context
import androidx.room.Room
import com.example.satellite.db.SatelliteDetailDatabase
import com.example.satellite.network.GsonHelper
import com.example.satellite.network.GsonHelperImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindGsonHelper(gsonHelper: GsonHelperImp): GsonHelper

    companion object {

        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext app: Context
        ) = Room.databaseBuilder(
            app,
            SatelliteDetailDatabase::class.java,
            "satellite_detail_database"
        ).build()

        @Singleton
        @Provides
        fun provideDao(db: SatelliteDetailDatabase) = db.satelliteDetailListDao()

    }
}