package com.example.satellite.view.satelliteDetail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.satellite.data.SatelliteDetailItem
import com.example.satellite.databinding.FragmentSatelliteDetailBinding
import com.example.satellite.viewmodel.SatelliteDetailViewModel

class SatelliteDetailFragment : Fragment() {

    lateinit var binding: FragmentSatelliteDetailBinding
    private lateinit var viewModel: SatelliteDetailViewModel
    var detail : SatelliteDetailItem? = null

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
        binding.satelliteDetail = detail
        return binding.root
    }

}