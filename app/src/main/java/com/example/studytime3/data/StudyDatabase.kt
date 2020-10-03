package com.example.studytime3.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studytime3.data.local.StudyDao
import com.example.studytime3.data.local.entities.StudySession

@Database(entities = [StudySession::class], version = 1)
abstract class StudyDatabase : RoomDatabase(){

    abstract fun studyDao() : StudyDao
}