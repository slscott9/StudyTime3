package com.example.studytime3.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studytime3.data.StudyDatabase
import com.example.studytime3.data.local.StudyDao
import com.example.studytime3.data.local.repos.StudyRepo
import com.example.studytime3.data.local.repos.StudyRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDao(db : StudyDatabase) = db.studyDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, StudyDatabase::class.java, "study_database_3")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideRepo(dao: StudyDao): StudyRepo = StudyRepoImpl(dao)

}