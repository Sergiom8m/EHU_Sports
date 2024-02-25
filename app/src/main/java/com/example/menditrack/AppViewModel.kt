package com.example.menditrack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.menditrack.data.Language


class AppViewModel: ViewModel() {

    private var actual_language by mutableStateOf(Language.ES)
        private set
    fun setLanguage(language: Language) {
        actual_language = language
    }

}