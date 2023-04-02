package com.example.satellite.view.satelliteList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.satellite.data.SatelliteList
import com.example.satellite.databinding.FragmentSatelliteListBinding
import com.example.satellite.utils.readAssetsFile
import com.example.satellite.viewmodel.SatelliteListViewModel
import com.google.gson.Gson


class SatelliteListFragment : Fragment() {

    lateinit var binding: FragmentSatelliteListBinding
    private lateinit var viewModel: SatelliteListViewModel
    private var satelliteList : SatelliteList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SatelliteListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSatelliteListBinding.inflate(inflater, container, false)
        getSatelliteList()
        setUi()
        return binding.root
    }

    private fun getSatelliteList(){
        satelliteList = Gson().fromJson(resources.assets.readAssetsFile("satellite-list.json"), SatelliteList::class.java)
    }

    private fun setUi(){
        binding.rvSatelliteList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvSatelliteList.adapter = satelliteList?.let { SatelliteListAdapter(it) }
    }

}