package com.example.satellite.view.satelliteList

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.satellite.data.SatelliteDetail
import com.example.satellite.data.SatelliteList
import com.example.satellite.data.SatelliteListItem
import com.example.satellite.databinding.FragmentSatelliteListBinding
import com.example.satellite.utils.readAssetsFile
import com.example.satellite.viewmodel.SatelliteListViewModel
import com.google.gson.Gson


class SatelliteListFragment : Fragment() {

    lateinit var binding: FragmentSatelliteListBinding
    private lateinit var viewModel: SatelliteListViewModel
    private var satelliteList: SatelliteList? = null
    private var satelliteDetail: SatelliteDetail? = null
    private var selectedItem: SatelliteListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SatelliteListViewModel(Application())::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSatelliteListBinding.inflate(inflater, container, false)
        getSatelliteList()
        setUi()
        viewModel.satelliteDetailList.observe(viewLifecycleOwner) {
            val detail = it.filter { list ->
                list.id == selectedItem?.id
            }
            val action =
                SatelliteListFragmentDirections.actionSatelliteListFragmentToSatelliteDetailFragment(
                    detail[0]
                )
            Navigation.findNavController(requireView()).navigate(action)
        }
        return binding.root
    }

    private fun getSatelliteList() {
        satelliteList = Gson().fromJson(
            resources.assets.readAssetsFile("satellite-list.json"),
            SatelliteList::class.java
        )
    }

    private fun getSatelliteDetailList() {
        satelliteDetail = Gson().fromJson(
            resources.assets.readAssetsFile("satellite-detail.json"),
            SatelliteDetail::class.java
        )
        satelliteDetail?.let { viewModel.storeInSQLite(it) }
    }

    private fun setUi() {
        binding.rvSatelliteList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvSatelliteList.adapter = satelliteList?.let { it ->
            SatelliteListAdapter(it) { item ->
                selectedItem = item
                viewModel.satelliteDetailList.value?.let {
                    viewModel.getSatelliteDetailFromDb()
                } ?: kotlin.run {
                    getSatelliteDetailList()
                }
            }
        }
    }

}