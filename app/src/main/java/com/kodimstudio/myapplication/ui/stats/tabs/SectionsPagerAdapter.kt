package com.kodimstudio.myapplication.ui.stats.tabs

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kodimstudio.myapplication.R

class SectionsPagerAdapter(
    fm: FragmentActivity
) : FragmentStateAdapter(fm) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return GeneralStatsFragment()
            1 -> return KillerStatsFragment()
            2 -> return SurvStatsFragment()
        }
        return GeneralStatsFragment()
    }

    override fun getItemCount(): Int {
        return 3
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.stats_tab_text_1,
            R.string.stats_tab_text_2,
            R.string.stats_tab_text_3
        )
    }
}