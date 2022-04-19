package com.android.testtask.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.testtask.fragments.ReportFragment
import com.android.testtask.fragments.StationsFragment

class ViewPagerFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val itemCount = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StationsFragment()
            else -> ReportFragment()
        }
    }

    override fun getItemCount(): Int = itemCount
}