package com.example.satellite.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.satellite.data.SatelliteDetailItem

@Dao
interface SatelliteDetailDao {

    @Insert
    suspend fun insertAll(vararg satelliteDetailItem: SatelliteDetailItem) :List<Long>

    @Query("SELECT * FROM satelliteDetailItem")
    suspend fun getAllSatellites():List<SatelliteDetailItem>

    @Query("DELETE FROM satelliteDetailItem")
    suspend fun deleteAllList()

}