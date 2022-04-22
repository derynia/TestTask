package com.android.testtask

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.android.testtask.adapters.ViewPagerFragmentStateAdapter
import com.android.testtask.databinding.ActivityMainBinding
import com.android.testtask.db.entity.Stations
import com.android.testtask.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private lateinit var binding: ActivityMainBinding

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val mainViewModel : MainViewModel by viewModels()
    private val tabNames = arrayOf("Stations","Report")
    private lateinit var stationsObserver: Observer<List<Stations>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setupView() {
        binding.viewPagerFragments.adapter = ViewPagerFragmentStateAdapter(this)
        TabLayoutMediator(binding.tabLayoutMain, binding.viewPagerFragments)
        { tab, position -> tab.text = tabNames[position] }.attach()

        stationsObserver = Observer<List<Stations>> { newStation ->
            runSyncService()
        }

        mainViewModel.stations.observeForever(stationsObserver)
        mainViewModel.getStations()
    }

    private fun runSyncService() {
        Intent(this, DbSyncService::class.java).also {
            startService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.stations.removeObserver(stationsObserver)
    }
}