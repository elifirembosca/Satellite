package com.example.satellite.view.satelliteDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.satellite.data.Position
import com.example.satellite.data.SatelliteDetailItem
import com.example.satellite.databinding.FragmentSatelliteDetailBinding
import com.example.satellite.viewmodel.SatelliteDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SatelliteDetailFragment : Fragment() {

    lateinit var binding: FragmentSatelliteDetailBinding
    private val viewModel: SatelliteDetailViewModel by viewModels()
    private var position: Position? = null

    private val positionTimer = (1..2)
        .asSequence()
        .asFlow()
        .onEach {
            delay(3000)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSatelliteDetailBinding.inflate(inflater, container, false)
        viewModel.getPositions()
        binding.satelliteDetail = requireArguments().get("satelliteDetail") as SatelliteDetailItem
        binding.satelliteName = requireArguments().getString("satelliteName")
        setUi()
        return binding.root
    }

    private fun setUi() {
        viewModel.positionsList.observe(viewLifecycleOwner) {
            position = it?.filter { list ->
                list.id == binding.satelliteDetail?.id.toString()
            }?.get(0)
            binding.positionData = "(${position?.positions?.get(0)?.posX}," +
                    "${position?.positions?.get(0)?.posY})"
            updatePositions()
        }
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
