package com.example.satellite.view.satelliteList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.satellite.R
import com.example.satellite.data.SatelliteListItem
import com.example.satellite.databinding.ItemSatelliteBinding

class SatelliteListAdapter(
    var satelliteList: ArrayList<SatelliteListItem>,
    var onClick: (SatelliteListItem) -> Unit
) : RecyclerView.Adapter<SatelliteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_satellite,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(holder.binding, satelliteList[position])
    }

    override fun getItemCount(): Int {
        return satelliteList.size
    }

    inner class ViewHolder(var binding: ItemSatelliteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            binding: ItemSatelliteBinding,
            item: SatelliteListItem
        ) {
            binding.run {
                this.satellite = item
            }
            binding.item.setOnClickListener {
                onClick.invoke(item)
            }
        }
    }

}
