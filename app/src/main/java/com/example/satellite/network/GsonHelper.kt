package com.example.satellite.network

import android.content.Context
import com.example.satellite.data.PositionsList
import com.example.satellite.data.SatelliteDetail
import com.example.satellite.data.SatelliteList
import com.example.satellite.utils.readAssetsFile
import com.google.gson.Gson

interface GsonHelper {

    fun getSatelliteDetailList(context: Context): SatelliteDetail =
        Gson().fromJson(
            context.resources.assets.readAssetsFile("satellite-detail.json"),
            SatelliteDetail::class.java
        )

    fun getSatelliteList(context: Context) : SatelliteList = Gson().fromJson(
        context.resources.assets.readAssetsFile("satellite-list.json"),
        SatelliteList::class.java
    )

    fun getPositions(context: Context) : PositionsList = Gson().fromJson(
        context.resources.assets.readAssetsFile("positions.json"),
        PositionsList::class.java
    )
}