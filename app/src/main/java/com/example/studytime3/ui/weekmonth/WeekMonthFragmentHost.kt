package com.example.studytime3.ui.weekmonth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.studytime3.R
import com.example.studytime3.databinding.FragmentWeekMonthHostBinding
import com.example.studytime3.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class WeekMonthFragmentHost : Fragment() {

    private lateinit var binding : FragmentWeekMonthHostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_week_month_host, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        // Set the icon and text for each tab
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)


        return binding.root
    }


    private fun getTabTitle(position: Int): String? {
        return when (position) {
            0 -> getString(R.string.week)
            1 -> getString(R.string.month)
            else -> null
        }
    }

}