package com.example.studytime3.ui.baseactivity

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.studytime3.data.local.entities.StudySession
import com.example.studytime3.data.local.repos.StudyRepo
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainActivityViewModel @ViewModelInject constructor(
    private val repo: StudyRepo,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val monthDayLabels = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
    private val nullLabels = arrayListOf<String>("No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data")
    private val months = arrayListOf<String>("January", "February" ,"March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    var month: String = ""

    private val currentMonth = LocalDateTime.now().monthValue
    private val currentDayOfMonth = LocalDateTime.now().dayOfMonth

    private val lastSevenSessionsHours = repo.getLastSevenSessionsHours(
       currentMonth, currentDayOfMonth
    ).asLiveData()

    private val lastSevenStudySessions = lastSevenSessionsHours.switchMap {
        repo.getLastSevenSessions(currentMonth, currentDayOfMonth).asLiveData()
    }

    val weekBarData = lastSevenStudySessions.map {
        setLastSevenSessionsBarData(it)
    }


    private val monthsSessionHours = repo.getSessionHoursForMonth(currentMonth).asLiveData()

    private val monthsStudySession = monthsSessionHours.switchMap {
        repo.getAllSessionsWithMatchingMonth(currentMonth).asLiveData()
    }

    val monthBarData = monthsStudySession.map {
        setSessionWithMonthBarData(it)
    }



    private fun setSessionWithMonthBarData(monthsStudySessionList: List<StudySession>) : BarData{

        val monthBarDataSetValues = MutableList(31) { BarEntry(0F, 0) }
        var monthBarData = BarData()

        if (monthsStudySessionList.isNullOrEmpty()) {
            val barDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            monthBarData = BarData(monthDayLabels, barDataSet)

        } else {
            //Entries uses the fixed size so we can add values to it at specific indexes
            //BarEntry(value, index) we can specify the index this bar value will be placed

            for (i in monthsStudySessionList.indices) {
                monthBarDataSetValues[monthsStudySessionList[i].dayOfMonth - 1] =
                    BarEntry(
                        monthsStudySessionList[i].hours,
                        monthsStudySessionList[i].dayOfMonth - 1
                    ) //to match the array indexes
            }

            val monthBarDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            month =
                months[monthsStudySessionList[0].month - 1] //set the month value to be displayed in the monthBarChart's description

            monthBarData = BarData(monthDayLabels, monthBarDataSet)
        }

        return monthBarData
    }


    private fun setLastSevenSessionsBarData(list: List<StudySession>): BarData {
        val weekBarDataSetValues = ArrayList<BarEntry>()
        var weekBarData = BarData()

        if (list.isNullOrEmpty()) {
            val barDataSet = BarDataSet(weekBarDataSetValues, "Sessions")
            weekBarData = BarData(nullLabels, barDataSet)

        } else {
            val datesFromSessions = ArrayList<String>()

            for (session in list.indices) {
                weekBarDataSetValues.add(
                    BarEntry(
                        list[session].hours,
                        session
                    )
                )
                datesFromSessions.add(list[session].date)
            }
            val weekBarDataSet = BarDataSet(weekBarDataSetValues, "Hours")
            weekBarData = BarData(datesFromSessions, weekBarDataSet)
        }

        return weekBarData
    }

    fun upsertStudySession(newStudySession: StudySession){
        GlobalScope.launch {
            repo.upsertStudySession(newStudySession)
        }
    }

}