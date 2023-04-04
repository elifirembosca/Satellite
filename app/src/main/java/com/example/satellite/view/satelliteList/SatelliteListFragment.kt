package com.example.satellite.view.satelliteList

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.satellite.MainActivity
import com.example.satellite.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = SearchView(
            (activity as MainActivity).supportActionBar!!.themedContext
        )
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
        item.actionView = searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var newList: ArrayList<SatelliteListItem> = ArrayList()
                for (row in viewModel.satelliteList.value!!) {
                    if (row.name.lowercase()
                            .contains(newText.lowercase())
                    ) {
                        newList.add(row)
                    } else if (newText.isBlank()) {
                        newList = viewModel.satelliteList.value!!
                    }
                }
                (binding.rvSatelliteList.adapter as SatelliteListAdapter).updateList(newList)
                return true
            }
        })
    }


}