package com.android.testtask

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.android.testtask.adapters.ViewPagerFragmentStateAdapter
import com.android.testtask.databinding.ActivityMainBinding
import com.android.testtask.db.entity.Stations
import com.android.testtask.utils.showError
import com.android.testtask.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private lateinit var binding: ActivityMainBinding

const val PERMISSION_REQUEST_LOCATION = 0

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val mainViewModel : MainViewModel by viewModels()
    private val tabNames = arrayOf("Stations","Report")
    private lateinit var stationsObserver: Observer<List<Stations>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestPermissionOnStart()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showError(this, getString(R.string.permissions_error), getString(R.string.geo_permission_denied))
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestPermissionOnStart() {
        if (checkSelfPermissionCompat(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestWritePermission()
        }
    }

    private fun requestWritePermission() {
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissionsCompat(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
        } else {
            requestPermissionsCompat(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
        }
    }
}