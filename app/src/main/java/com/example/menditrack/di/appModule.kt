package com.example.menditrack.di

import android.content.Context
import androidx.room.Room
import com.example.menditrack.model.ActivityDao
import com.example.menditrack.model.ActivityRepository
import com.example.menditrack.model.Database
import com.example.menditrack.model.IActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object appModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context)=
        Room.databaseBuilder(app, Database::class.java,"ehuSportsDB.db").fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideActivityDao(db: Database) = db.activityDao()

    @Singleton
    @Provides
    fun provideActivityRepository(activityDao: ActivityDao): IActivityRepository = ActivityRepository(activityDao)
}