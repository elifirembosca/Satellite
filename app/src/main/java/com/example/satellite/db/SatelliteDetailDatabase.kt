package com.example.satellite.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.satellite.data.SatelliteDetailItem

@Database(entities = [SatelliteDetailItem::class], version = 1)
abstract class SatelliteDetailDatabase : RoomDatabase() {

    abstract fun satelliteDetailListDao(): SatelliteDetailDao

    companion object {

        @Volatile
        private var instance: SatelliteDetailDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, SatelliteDetailDatabase::class.java, "satelliteDetailDatabase"
        ).build()
    }
}