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
            textSupplier.text = station.supplier
            textQty.text = station.qty.toString()
            textSum.text = dec.format(station.sum)
            textAddress.text = station.address
            imageDelete.setOnClickListener{
                onDeleteClick(station)
            }
        }

        itemView.setOnClickListener { onItemClick(station) }
    }

    companion object {
        val dec = DecimalFormat("#.00")
    }
}