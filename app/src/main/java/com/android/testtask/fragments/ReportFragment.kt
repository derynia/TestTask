package com.android.testtask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.testtask.R
import com.android.testtask.adapters.ReportsAdapter
import com.android.testtask.databinding.FragmentReportBinding
import com.android.testtask.db.entity.ReportData
import com.android.testtask.viewmodel.ReportsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment(R.layout.fragment_report) {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val reportsViewModel : ReportsViewModel by viewModels()
    private val reportsAdapter = ReportsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onResume() {
        super.onResume()
        reportsViewModel.getReportData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.buttonRefresh.setOnClickListener {
            reportsViewModel.getReportData()
        }

        binding.recyclerReport.adapter = reportsAdapter
        val reportsObserver = Observer<List<ReportData>> { newData ->
            reportsAdapter.setList(newData)
        }

        reportsViewModel.stations.observe(viewLifecycleOwner, reportsObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}