package com.example.menditrack.preferences

import androidx.core.os.LocaleListCompat
import com.example.menditrack.data.Language
import javax.inject.Inject
import javax.inject.Singleton
import androidx.appcompat.app.AppCompatDelegate

// Singleton class to manage the language preferences
@Singleton
class LanguageManager @Inject constructor() {
    // Method to change the App's language setting a new locale
    fun changeLang(lang: Language) {
        val localeList = LocaleListCompat.forLanguageTags(lang.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}