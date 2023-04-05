package com.example.satellite.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.satellite.data.SatelliteDetailItem

@Database(entities = [SatelliteDetailItem::class], version = 1)
abstract class SatelliteDetailDatabase : RoomDatabase() {

    abstract fun satelliteDetailListDao(): SatelliteDetailDao

}