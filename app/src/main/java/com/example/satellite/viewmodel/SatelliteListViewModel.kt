package com.example.satellite.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.satellite.data.SatelliteDetailItem
import com.example.satellite.data.SatelliteListItem
import com.example.satellite.db.SatelliteDetailDatabase
import com.example.satellite.utils.SingleLiveData
import kotlinx.coroutines.launch

class SatelliteListViewModel(application: Application) : AndroidViewModel(application) {

    var satelliteDetailList = SingleLiveData<List<SatelliteDetailItem>>()
    var satelliteList = MutableLiveData<ArrayList<SatelliteListItem>>()

     fun getSatelliteDetailFromDb() {
        viewModelScope.launch {
            val satelliteDetail = SatelliteDetailDatabase(getApplication()).satelliteDetailListDao()
                .getAllSatellites()
            satelliteDetailList.value = satelliteDetail
            Toast.makeText(getApplication(), "Detail From Db", Toast.LENGTH_SHORT).show()
        }
    }

     fun storeInSQLite(list: List<SatelliteDetailItem>) {
        viewModelScope.launch {
            val dao = SatelliteDetailDatabase(getApplication()).satelliteDetailListDao()
            dao.deleteAllList()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i += 1
            }
            satelliteDetailList.value = list
            Toast.makeText(getApplication(), "Detail From Api", Toast.LENGTH_SHORT).show()
        }
    }
}