package com.example.studytime3.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.studytime3.ui.monthview.MonthViewFragment
import com.example.studytime3.ui.sessionselector.SessionSelectorFragment
import com.example.studytime3.ui.timer.TimerFragment
import com.example.studytime3.ui.weekview.WeekViewFragment

class ViewPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2



    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return when(position){
            0 -> WeekViewFragment()
            else -> MonthViewFragment()

        }
    }


}