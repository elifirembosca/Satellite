package com.example.satellite.view.satelliteDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.satellite.data.Position
import com.example.satellite.data.PositionsList
import com.example.satellite.data.SatelliteDetailItem
import com.example.satellite.databinding.FragmentSatelliteDetailBinding
import com.example.satellite.utils.readAssetsFile
import com.example.satellite.viewmodel.SatelliteDetailViewModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

class SatelliteDetailFragment : Fragment() {

    lateinit var binding: FragmentSatelliteDetailBinding
    private lateinit var viewModel: SatelliteDetailViewModel
    private var detail: SatelliteDetailItem? = null
    private var position: Position? = null

    private val positionTimer = (1..2)
        .asSequence()
        .asFlow()
        .onEach {
            delay(3000)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SatelliteDetailViewModel()::class.java]
        detail = requireArguments().get("satelliteDetail") as SatelliteDetailItem
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSatelliteDetailBinding.inflate(inflater, container, false)
        getPositions()
        binding.satelliteDetail = detail
        binding.satelliteName = requireArguments().getString("satelliteName")
        return binding.root
    }

    private fun getPositions() {
        val positionList = Gson().fromJson(
            resources.assets.readAssetsFile("positions.json"),
            PositionsList::class.java
        )
        position = positionList?.list?.filter { list ->
            list.id == detail?.id.toString()
        }?.get(0)
        binding.positionData = "(${position?.positions?.get(0)?.posX}," +
                "${position?.positions?.get(0)?.posY})"
        updatePositions()
    }


    private fun updatePositions(): Job {
        return lifecycleScope.launch {
            positionTimer.collect {
                binding.positionData = "(${position?.positions?.get(it)?.posX}," +
                        "${position?.positions?.get(it)?.posY})"
            }
        }
    }
}
