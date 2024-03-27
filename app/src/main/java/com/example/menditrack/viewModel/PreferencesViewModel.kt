package com.example.menditrack.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menditrack.data.Language
import com.example.menditrack.preferences.IPreferencesRepository
import com.example.menditrack.preferences.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: IPreferencesRepository,
    private val languageManager: LanguageManager
): ViewModel(){

    // Preference variables
    val currentSetLang by languageManager::currentLang
    val lang = preferencesRepository.getLanguage().map { Language.getFromCode(it) }
    val theme = preferencesRepository.getThemePreferences()

    // Functions to save preferences when the user changes them (selected language + theme)
    fun changeLang(lang: Language) {
        languageManager.changeLang(lang)
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.setLanguage(lang.code) }
    }

    fun restartLang(lang: Language){
        languageManager.changeLang(lang)
    }
    fun changeTheme(color: Int) {
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.setThemePreferences(color) }
    }
}