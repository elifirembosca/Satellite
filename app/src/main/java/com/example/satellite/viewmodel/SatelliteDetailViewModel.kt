package com.example.satellite.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.satellite.data.Position
import com.example.satellite.db.SatelliteRepository
import com.example.satellite.network.GsonRepository
import com.example.satellite.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SatelliteDetailViewModel @Inject constructor(
    private val dbRepository: SatelliteRepository,
    private val gsonRepository: GsonRepository
) : ViewModel() {

    val positionsList = SingleLiveData<List<Position>>()

    fun getPositions() {
        gsonRepository.getPositions({
            positionsList.value = it.list
        }, {
            Log.e("getPositions","error")
        })
    }

}