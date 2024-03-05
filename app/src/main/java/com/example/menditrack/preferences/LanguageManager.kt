package com.example.menditrack.preferences

import androidx.core.os.LocaleListCompat
import com.example.menditrack.data.Language
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import androidx.appcompat.app.AppCompatDelegate

@Singleton
class LanguageManager @Inject constructor() {

    // Current application's lang
    var currentLang: Language = Language.getFromCode(Locale.getDefault().language.lowercase())

    // Method to change the App's language setting a new locale
    fun changeLang(lang: Language) {
        val localeList = LocaleListCompat.forLanguageTags(lang.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}