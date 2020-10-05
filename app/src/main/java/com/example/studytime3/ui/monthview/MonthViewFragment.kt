package com.example.studytime3.ui.monthview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.studytime3.R
import com.example.studytime3.databinding.FragmentMonthViewBinding
import com.example.studytime3.ui.baseactivity.MainActivityViewModel
import com.example.studytime3.ui.weekmonth.WeekMonthFragmentHostDirections
import com.github.mikephil.charting.data.BarData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonthViewFragment : Fragment() {

    private lateinit var binding: FragmentMonthViewBinding
    private val viewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_month_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.monthBarData.observe(viewLifecycleOwner){
            it?.let {
                setBarChart(it)
            }
        }

        //Flow will only emit when terminal operator is called such as collect
//        viewModel.viewModelScope.launch {
//            viewModel.monthBarData.collect {
//                setBarChart(it)
//            }
//        }
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



    private fun setBarChart(barData: BarData) {
        val xaxis = binding.monthBarChart.xAxis //sets the spacing between the x labels
        xaxis.spaceBetweenLabels = 0

        binding.monthBarChart.fitScreen()
        binding.monthBarChart.data = barData // set the data and list of lables into chart
        binding.monthBarChart.setDescription(viewModel.month)

//        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
//        barDataSet.color = resources.getColor(R.color.colorAccent)

        binding.monthBarChart.animateY(1000)
    }


}