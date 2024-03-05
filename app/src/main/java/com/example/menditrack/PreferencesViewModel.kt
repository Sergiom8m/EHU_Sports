package com.example.menditrack

import android.content.Context
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

    val currentSetLang by languageManager::currentLang
    val lang = preferencesRepository.getLanguage().map { Language.getFromCode(it) }

    fun changeLang(idioma: Language, context: Context) {
        languageManager.changeLang(idioma)
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.setLanguage(idioma.code) }
    }

}