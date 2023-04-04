package com.example.satellite.view.satelliteList

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
        return binding.root
    }

    private fun getSatelliteList() {
        val satelliteList = Gson().fromJson(
            resources.assets.readAssetsFile("satellite-list.json"),
            SatelliteList::class.java
        )
        //added 4ms delay to show progress bar
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.satelliteList.postValue(satelliteList)
        }, 400)
    }

    private fun getSatelliteDetailList() {
        satelliteDetail = Gson().fromJson(
            resources.assets.readAssetsFile("satellite-detail.json"),
            SatelliteDetail::class.java
        )
        satelliteDetail?.let { viewModel.storeInSQLite(it) }
    }

    private fun setUi() {
        viewModel.satelliteDetailList.observe(viewLifecycleOwner) {
            val detail = it.filter { list ->
                list.id == selectedItem?.id
            }
            val action =
                SatelliteListFragmentDirections.actionSatelliteListFragmentToSatelliteDetailFragment(
                    detail[0], selectedItem?.name
                )
            Navigation.findNavController(requireView()).navigate(action)
        }
        viewModel.satelliteList.observe(viewLifecycleOwner) {
            binding.loading.isVisible = false
            binding.rvSatelliteList.adapter = SatelliteListAdapter(it) { item ->
                selectedItem = item
                viewModel.satelliteDetailList.value?.let {
                    viewModel.getSatelliteDetailFromDb()
                } ?: kotlin.run {
                    getSatelliteDetailList()
                }
            }
        }
        binding.rvSatelliteList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

}