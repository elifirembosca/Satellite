package com.example.satellite.db

import android.content.Context
import com.example.satellite.data.SatelliteDetailItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DbController(context: Context) {
    val dao = SatelliteDetailDatabase(context).satelliteDetailListDao()

    fun insertAll(list: List<SatelliteDetailItem>, onInsertFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllList()
            dao.insertAll(*list.toTypedArray())
            onInsertFinished.invoke()
        }
    }

    fun getSatelliteDetailFromDb(onSatellitesDetailLoaded: (data: List<SatelliteDetailItem>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = dao.getAllSatellites()
            onSatellitesDetailLoaded(list)
        }
    }

}