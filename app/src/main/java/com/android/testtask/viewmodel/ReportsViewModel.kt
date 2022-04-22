package com.android.testtask.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.android.testtask.db.entity.ReportData
import com.android.testtask.db.repo.StationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val stationsRepo: StationsRepo,
    ) : ViewModel() {

    val stations : MediatorLiveData<List<ReportData>> by lazy {
        MediatorLiveData<List<ReportData>>()
    }

    fun getReportData() = stations.addSource(
        stationsRepo.reportList()
    ) {
        stations.value = it
    }
}