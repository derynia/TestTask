package com.android.testtask.adapters

import androidx.recyclerview.widget.DiffUtil
import com.android.testtask.db.entity.Stations

class StationsComparator: DiffUtil.ItemCallback<Stations>() {

    override fun areItemsTheSame(oldItem: Stations, newItem: Stations) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Stations, newItem: Stations) = oldItem.id == newItem.id
}
