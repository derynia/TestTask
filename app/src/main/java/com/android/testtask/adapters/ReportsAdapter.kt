package com.android.testtask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.android.testtask.databinding.CardStationsReportItemBinding
import com.android.testtask.db.entity.ReportData

class ReportsAdapter : ListAdapter<ReportData, ReportsViewHolder>(ReportsComparator()) {

    fun setList(reportData: List<ReportData>?) {
        reportData?.let {
            submitList(reportData)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        val binding = CardStationsReportItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReportsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        val reportData = getItem(position)
        holder.bind(reportData)
    }
}