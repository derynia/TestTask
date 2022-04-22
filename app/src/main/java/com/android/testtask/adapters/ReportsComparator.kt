package com.android.testtask.adapters

import androidx.recyclerview.widget.DiffUtil
import com.android.testtask.db.entity.ReportData

class ReportsComparator: DiffUtil.ItemCallback<ReportData>() {

    override fun areItemsTheSame(oldItem: ReportData, newItem: ReportData) = oldItem == newItem

    override fun areContentsTheSame(oldItem: ReportData, newItem: ReportData) = oldItem.address == newItem.address
}
