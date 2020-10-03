package com.example.studytime3.ui.sessionselector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.studytime3.R
import com.example.studytime3.databinding.FragmentSessionSelectorBinding
import com.example.studytime3.ui.adapters.MonthsSessionsAdapter
import com.example.studytime3.ui.adapters.YearsSessionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionSelectorFragment : Fragment() {

    private lateinit var binding : FragmentSessionSelectorBinding
    private val viewModel : SessionSelectorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session_selector, container, false)
        binding.lifecycleOwner = viewLifecycleOwner



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearListAdapter = YearsSessionsAdapter() {
            viewModel.setYearSelected(it)
        }

        val monthListAdapter = MonthsSessionsAdapter {
            findNavController().navigate(SessionSelectorFragmentDirections.actionSessionSelectorFragmentToMonthDetailFragment(it))
        }


        viewModel.monthsWithSessions.observe(viewLifecycleOwner, Observer {
            it?.let {
                monthListAdapter.setData(it)
            }
        })

        viewModel.yearsWithSessions.observe(viewLifecycleOwner, Observer {
            it?.let {
                yearListAdapter.setData(it)
            }
        })

        binding.monthRecyclerView.adapter = monthListAdapter
        binding.yearsListRecyclerView.adapter = yearListAdapter
    }


}