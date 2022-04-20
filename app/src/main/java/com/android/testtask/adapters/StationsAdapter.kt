package com.android.testtask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.android.testtask.databinding.CardStationsRecyclerItemBinding
import com.android.testtask.db.entity.Stations

class StationsAdapter(
    private val onItemClick: (Stations) -> Unit
) : ListAdapter<Stations, StationsViewHolder>(StationsComparator()) {

    fun setList(stations: List<Stations>?) {
        stations?.let {
            submitList(stations)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsViewHolder {
        val binding = CardStationsRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationsViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, onItemClick)
    }
}