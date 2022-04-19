package com.android.testtask

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.android.testtask.adapters.ViewPagerFragmentStateAdapter
import com.android.testtask.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private lateinit var binding: ActivityMainBinding

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val tabNames = arrayOf("Stations","Report")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        binding.viewPagerFragments.adapter = ViewPagerFragmentStateAdapter(this)
        TabLayoutMediator(binding.tabLayoutMain, binding.viewPagerFragments)
        { tab, position -> tab.text = tabNames[position] }.attach()
    }
}