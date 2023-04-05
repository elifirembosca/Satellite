package com.example.satellite.db

import com.example.satellite.data.SatelliteDetailItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SatelliteRepository @Inject constructor(private val dao: SatelliteDetailDao) {

    suspend fun insertAll(list: List<SatelliteDetailItem>, onInsertFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllList()
            dao.insertAll(*list.toTypedArray())
            onInsertFinished.invoke()
        }
    }

    suspend fun getSatelliteDetailFromDb(onSatellitesDetailLoaded: (data: List<SatelliteDetailItem>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = dao.getAllSatellites()
            onSatellitesDetailLoaded(list)
        }
    }

}