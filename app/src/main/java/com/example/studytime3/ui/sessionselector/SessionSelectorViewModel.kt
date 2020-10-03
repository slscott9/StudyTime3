package com.example.studytime3.ui.sessionselector

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.studytime3.data.local.repos.StudyRepo
import kotlinx.coroutines.Dispatchers

class SessionSelectorViewModel @ViewModelInject constructor(
    private val repo: StudyRepo
) : ViewModel(){

     val yearsWithSessions = repo.getYearsWithSessions().asLiveData()

    private val _yearSelected = MutableLiveData<Int>()

    val monthsWithSessions = _yearSelected.switchMap {
        repo.getMonthsWithSelectedYear(it).asLiveData()
    }

     fun setYearSelected(yearSelected : Int) {
        _yearSelected.value = yearSelected
    }

}