package com.example.menditrack.di

import android.content.Context
import androidx.room.Room
import com.example.menditrack.model.ActivityDao
import com.example.menditrack.model.ActivityRepository
import com.example.menditrack.model.Database
import com.example.menditrack.model.IActivityRepository
import com.example.menditrack.preferences.IPreferencesRepository
import com.example.menditrack.preferences.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Object to define Singleton entities (PROVIDER METHOD)
@Module
@InstallIn(SingletonComponent::class)
object appModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context)=
        Room.databaseBuilder(app, Database::class.java,"ehuSportsDB")
            // Instructions to populate the DB with external .db file
            .createFromAsset("database/ehuSports.db")
            .build()

    @Singleton
    @Provides
    fun provideActivityDao(db: Database) = db.activityDao()

    @Singleton
    @Provides
    fun provideActivityRepository(activityDao: ActivityDao): IActivityRepository = ActivityRepository(activityDao)

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext app: Context): IPreferencesRepository = PreferencesRepository(app)
}