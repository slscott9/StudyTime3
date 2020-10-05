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

/*
    Changed flow test
 */

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


    //by giving .asLiveData view model scope this view models live data is only calculated once when the view model is live.
    //When a change occurs the observers are notified and update their views
    //without scope every time month or week view is instantiated the live data is recalculated because
    //it does not have a scope to make it life cycle aware
    //so scope tells live data and coroutines when to calculate -> within this view models lifecycle
    //coroutines will be cancelled when this view model is destroyed

    /*
        Think of twitter example maybe the flow should be the lastSevensessionsHours since this is not one shot operation
        and changes like likes and retweets

        The one shot operation would be the lastSevenStudySessions

        Its okay to emit all seven session all at once. but hours could change today since hours will keep being inserted into database
         so it needs to be flow (stream of hours updates each time new hours are added
     */

    private val lastSevenSessionsHours = repo.getLastSevenSessionsHours(
       currentMonth, currentDayOfMonth
    ).asLiveData(viewModelScope.coroutineContext)

    private val lastSevenStudySessions = lastSevenSessionsHours.switchMap {
        repo.getLastSevenSessions(currentMonth, currentDayOfMonth)
            .asLiveData(viewModelScope.coroutineContext)
    }

    val weekBarData = lastSevenStudySessions.map {
        setLastSevenSessionsBarData(it)
    }


    private val monthsSessionHours = repo.getSessionHoursForMonth(currentMonth)
        .asLiveData(viewModelScope.coroutineContext)

    private val monthsStudySession = monthsSessionHours.switchMap {
        repo.getAllSessionsWithMatchingMonth(currentMonth)
            .asLiveData(viewModelScope.coroutineContext)
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