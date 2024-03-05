package com.example.menditrack.preferences

import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

interface IPreferencesRepository {

    fun getLanguage(): Flow<String>

    suspend fun setLanguage(code: String)

    fun getThemePreferences(): Flow<Int>

    suspend fun setThemePreferences(theme: Int)
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PREFERENCES_SETTINGS")

@Singleton
class PreferencesRepository @Inject constructor(
    private val context: Context
): IPreferencesRepository{

    val PREFERENCE_LANGUAGE = stringPreferencesKey("preference_lang")
    val PREFERENCE_THEME = intPreferencesKey("preference_theme")


    override fun getLanguage(): Flow<String> = context.dataStore.data.map { preferences -> preferences[PREFERENCE_LANGUAGE]?: Locale.getDefault().language }
    override suspend fun setLanguage(code: String) {
        context.dataStore.edit { settings ->  settings[PREFERENCE_LANGUAGE]=code}
    }

    override fun getThemePreferences(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[PREFERENCE_THEME] ?: 0
        }
    }

    override suspend fun setThemePreferences(theme: Int) {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCE_THEME] = theme
        }
    }

}