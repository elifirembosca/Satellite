package com.example.satellite.viewmodel

import androidx.lifecycle.ViewModel
import com.example.satellite.data.SatelliteDetailItem
import com.example.satellite.data.SatelliteListItem
import com.example.satellite.db.SatelliteRepository
import com.example.satellite.network.GsonRepository
import com.example.satellite.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SatelliteListViewModel @Inject constructor(
    private val dbRepository: SatelliteRepository,
    private val gsonRepository: GsonRepository
) : ViewModel() {

    var satelliteDetailList = SingleLiveData<List<SatelliteDetailItem>>()
    var satelliteList = SingleLiveData<ArrayList<SatelliteListItem>>()
    val satelliteListError = SingleLiveData<Boolean>()
    val satelliteListLoading = SingleLiveData<Boolean>()

    fun getSatelliteDetailFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            dbRepository.getSatelliteDetailFromDb {
                satelliteDetailList.postValue(it)
            }
        }
    }

    private fun insertSatelliteDetailList(list: List<SatelliteDetailItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            dbRepository.insertAll(list) {
                getSatelliteDetailFromDb()
            }
        }
    }

    private fun showSatellites(list: ArrayList<SatelliteListItem>) {
        satelliteList.value = list
        satelliteListError.value = false
        satelliteListLoading.value = false
    }

    private fun showError() {
        satelliteListError.value = true
        satelliteListLoading.value = false
    }

    fun getSatelliteList() {
        gsonRepository.getSatelliteList({
            showSatellites(it)
        }, {
            showError()
        })
    }

    fun getSatelliteDetailList() {
        gsonRepository.getSatelliteDetailList({
            insertSatelliteDetailList(it)
        }, {
            showError()
        })
    }
}