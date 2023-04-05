package com.example.satellite.view.satelliteList

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.satellite.MainActivity
import com.example.satellite.R
import com.example.satellite.data.SatelliteListItem
import com.example.satellite.databinding.FragmentSatelliteListBinding
import com.example.satellite.viewmodel.SatelliteListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SatelliteListFragment : Fragment() {

    lateinit var binding: FragmentSatelliteListBinding
    private var selectedItem: SatelliteListItem? = null
    private val viewModel: SatelliteListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSatelliteListBinding.inflate(inflater, container, false)
        viewModel.getSatelliteList()
        setUi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setUi() {
        viewModel.satelliteDetailList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                viewModel.getSatelliteDetailList()
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