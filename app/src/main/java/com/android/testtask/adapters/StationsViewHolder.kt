package com.android.testtask.adapters

import androidx.recyclerview.widget.RecyclerView
import com.android.testtask.databinding.CardStationsRecyclerItemBinding
import com.android.testtask.db.entity.Stations
import java.text.DecimalFormat

class StationsViewHolder(private val binding: CardStationsRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private fun onItemClick(station: Stations, onButtonClick: (Stations) -> Unit) {
        onButtonClick(station)
    }

    private fun onDeleteClick(station: Stations, onButtonClick: (Stations) -> Unit) {
        onButtonClick(station)
    }

    fun bind(
        station: Stations,
        onItemClick : (Stations) -> Unit,
        onDeleteClick : (Stations) -> Unit
    )
    {
        with (binding) {
            binding.textSupplier.text = station.supplier
            binding.textQty.text = station.qty.toString()
            binding.textSum.text = dec.format(station.sum)
            binding.textAddress.text = station.address
            binding.imageDelete.setOnClickListener{
                onDeleteClick(station)
            }
        }

        itemView.setOnClickListener { onItemClick(station) }
    }

    companion object {
        val dec = DecimalFormat("#.00")
    }
}