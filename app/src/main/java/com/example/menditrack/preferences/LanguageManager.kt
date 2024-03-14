package com.example.menditrack.preferences

import androidx.core.os.LocaleListCompat
import com.example.menditrack.data.Language
import javax.inject.Inject
import javax.inject.Singleton
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

// Singleton class to manage the language preferences
@Singleton
class LanguageManager @Inject constructor() {
    // Method to change the App's language setting a new locale

    var currentLang: Language = Language.getFromCode(Locale.getDefault().language.lowercase())
    fun changeLang(lang: Language) {
        currentLang = lang
        val localeList = LocaleListCompat.forLanguageTags(lang.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}