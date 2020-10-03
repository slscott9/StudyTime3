package com.example.studytime3.data.local.repos

import com.example.studytime3.data.local.StudyDao
import com.example.studytime3.data.local.entities.StudySession
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudyRepoImpl @Inject constructor(
    private val dao: StudyDao
) : StudyRepo{

    override fun getLastSevenSessionsHours(
        currentMonth: Int,
        currentDayOfMonth: Int
    ): Flow<List<Float>> {
        return dao.getLastSevenSessionsHours(currentMonth, currentDayOfMonth)
    }

    override fun getSessionHoursForMonth(monthSelected: Int): Flow<List<Float>> {
        return dao.getSessionHoursForMonth(monthSelected)

    }

    override suspend fun getCurrentStudySession(currentDate: String): StudySession {
        return dao.getCurrentStudySession(currentDate)

    }

    override fun getAllSessionsWithMatchingMonth(monthSelected: Int): Flow<List<StudySession>> {
        return dao.getAllSessionsWithMatchingMonth(monthSelected)

    }

    override fun getLastSevenSessions(
        currentMonth: Int,
        currentDayOfMonth: Int
    ): Flow<List<StudySession>> {
        return dao.getLastSevenSessions(currentMonth, currentDayOfMonth)

    }

    override fun getYearsWithSessions(): Flow<List<Int>> {
        return dao.getYearsWithSessions()

    }

    override fun getMonthsWithSelectedYear(yearSelected: Int): Flow<List<Int>> {
        return dao.getMonthsWithSelectedYear(yearSelected)

    }

    override suspend fun upsertStudySession(studySession: StudySession) {
        dao.upsertStudySession(studySession)

    }


}