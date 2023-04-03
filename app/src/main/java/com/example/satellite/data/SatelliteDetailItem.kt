package com.example.satellite.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class SatelliteDetailItem(
    @ColumnInfo(name = "costPerLaunch")
    val cost_per_launch: Int,
    @ColumnInfo(name = "firstFlight")
    val first_flight: String,
    @ColumnInfo(name = "height")
    val height: Int,
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "mass")
    val mass: Int
): Serializable{
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}