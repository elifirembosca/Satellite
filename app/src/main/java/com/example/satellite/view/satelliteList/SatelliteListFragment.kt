package com.example.satellite.view.satelliteList

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
import com.example.satellite.db.DbController
import com.example.satellite.utils.readAssetsFile
import com.example.satellite.viewmodel.SatelliteListViewModel
import com.google.gson.Gson
import com.google.gson.JsonParseException


class SatelliteListFragment : Fragment() {

    lateinit var binding: FragmentSatelliteListBinding
    private lateinit var viewModel: SatelliteListViewModel
    private var satelliteDetail: SatelliteDetail? = null
    private var selectedItem: SatelliteListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SatelliteListViewModel()::class.java]
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
        viewModel.dbController = DbController(requireContext())
    }

    private fun getSatelliteList() {
        try {
            val satelliteList = Gson().fromJson(
                resources.assets.readAssetsFile("satellite-list.json"),
                SatelliteList::class.java
            )
            //added 4ms delay to show progress bar
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.showSatellites(satelliteList)
            }, 400)
        } catch (e: JsonParseException) {
            viewModel.showError()
        }

    }

    private fun getSatelliteDetailList() {
        satelliteDetail = Gson().fromJson(
            resources.assets.readAssetsFile("satellite-detail.json"),
            SatelliteDetail::class.java
        )
        satelliteDetail?.let { viewModel.insertSatelliteDetailList(it) }
    }

    private fun setUi() {
        viewModel.satelliteDetailList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                getSatelliteDetailList()
            } else {
                val detail = it.filter { list ->
                    list.id == selectedItem?.id
                }
                val action =
                    SatelliteListFragmentDirections.actionSatelliteListFragmentToSatelliteDetailFragment(
                        detail[0], selectedItem?.name
                    )
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
        viewModel.satelliteList.observe(viewLifecycleOwner) {
            setRecyclerView(it)
            viewModel.satelliteListLoading.observe(viewLifecycleOwner) {
                binding.loading.isVisible = it
            }
            viewModel.satelliteListError.observe(viewLifecycleOwner) {
                binding.error.isVisible = it
            }
            binding.rvSatelliteList.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
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
                setRecyclerView(newList)
                return true
            }
        })
    }

    fun setRecyclerView(list: ArrayList<SatelliteListItem>) {
        binding.rvSatelliteList.adapter = SatelliteListAdapter(list) { item ->
            selectedItem = item
            viewModel.getSatelliteDetailFromDb()
        }
    }

}