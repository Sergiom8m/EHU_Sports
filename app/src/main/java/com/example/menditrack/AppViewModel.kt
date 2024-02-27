package com.example.menditrack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.menditrack.data.Language


class AppViewModel: ViewModel() {

    var actual_language by mutableStateOf(Language.ES)
    var showAddButton by mutableStateOf(true)
    var enableNavigationButtons by mutableStateOf(true)




}