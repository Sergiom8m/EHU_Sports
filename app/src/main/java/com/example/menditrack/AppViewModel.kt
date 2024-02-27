package com.example.menditrack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.menditrack.data.Language


class AppViewModel: ViewModel() {

    var actual_language by mutableStateOf(Language.ES)
        private set

    var showAddButton by mutableStateOf(true)
        private set

    var enableNavigationButtons by mutableStateOf(true)


    fun setLanguage(language: Language) {
        actual_language = language
    }

    fun setAddButtonVisibility(visible: Boolean){
        showAddButton = visible
    }


}