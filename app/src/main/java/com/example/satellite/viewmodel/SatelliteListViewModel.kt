package com.example.satellite.viewmodel

import androidx.lifecycle.ViewModel
import com.example.satellite.data.SatelliteDetailItem
import com.example.satellite.data.SatelliteListItem
import com.example.satellite.db.DbController
import com.example.satellite.utils.SingleLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SatelliteListViewModel :ViewModel() {

    var satelliteDetailList = SingleLiveData<List<SatelliteDetailItem>>()
    var satelliteList = SingleLiveData<ArrayList<SatelliteListItem>>()
    val satelliteListError = SingleLiveData<Boolean>()
    val satelliteListLoading = SingleLiveData<Boolean>()
    lateinit var dbController : DbController


    fun getSatelliteDetailFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            dbController.getSatelliteDetailFromDb {
                satelliteDetailList.postValue(it)
            }
        }
    }

    fun insertSatelliteDetailList(list: List<SatelliteDetailItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            dbController.insertAll(list) {
                getSatelliteDetailFromDb()
            }
        }
    }

    fun showSatellites(list: ArrayList<SatelliteListItem>) {
        satelliteList.value = list
        satelliteListError.value = false
        satelliteListLoading.value = false
    }

    fun showError() {
        satelliteListError.value = true
        satelliteListLoading.value = false
    }
}