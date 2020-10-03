package com.example.studytime3.ui.weekmonth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.studytime3.R
import com.example.studytime3.databinding.FragmentWeekMonthHostBinding
import com.example.studytime3.ui.adapters.ViewPagerAdapter


class WeekMonthFragmentHost : Fragment() {

    private lateinit var viewPager : ViewPager2
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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addSessionChip.setOnClickListener {
            findNavController().navigate(WeekMonthFragmentHostDirections.actionWeekMonthFragmentHostToTimerFragment())
        }

        binding.sessionsChip.setOnClickListener {
            findNavController().navigate(WeekMonthFragmentHostDirections.actionWeekMonthFragmentHostToSessionSelectorFragment())
        }
    }

}