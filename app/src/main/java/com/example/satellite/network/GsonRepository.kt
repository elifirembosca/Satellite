package com.example.satellite.network

import android.content.Context
import com.example.satellite.data.PositionsList
import com.example.satellite.data.SatelliteDetail
import com.example.satellite.data.SatelliteList
import com.google.gson.JsonParseException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GsonRepository @Inject constructor(@ApplicationContext val context: Context, private val gsonHelper: GsonHelper) {

    fun getSatelliteDetailList(
        success: (SatelliteDetail) -> Unit,
        error: () -> Unit,
    ) {
        try {
            success(gsonHelper.getSatelliteDetailList(context))
        } catch (e: JsonParseException) {
            error()
        }
    }

    fun getSatelliteList(
        success: (SatelliteList) -> Unit,
        error: () -> Unit,
    ) {
        try {
            success(gsonHelper.getSatelliteList(context))
        } catch (e: JsonParseException) {
            error()
        }
    }

    fun getPositions(
        success: (PositionsList) -> Unit,
        error: () -> Unit,
    ) {
        try {
            success(gsonHelper.getPositions(context))
        } catch (e: JsonParseException) {
            error()
        }
    }
}