package com.android.testtask.adapters

import androidx.recyclerview.widget.RecyclerView
import com.android.testtask.databinding.CardStationsReportItemBinding
import com.android.testtask.db.entity.ReportData
import java.text.DecimalFormat

class ReportsViewHolder(private val binding: CardStationsReportItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        reportData: ReportData
    )
    {
        with (binding) {
            textQty.text = reportData.qty.toString()
            textSum.text = dec.format(reportData.sum)
            textAddressRep.text = reportData.address
        }
    }

    companion object {
        val dec = DecimalFormat("#.00")
    }
}