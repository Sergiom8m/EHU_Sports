package com.example.menditrack.viewModel


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.menditrack.model.IActivityRepository
import com.example.menditrack.data.SportActivity
import com.example.menditrack.utils.generateRandomId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.menditrack.utils.*

@HiltViewModel
class AppViewModel @Inject constructor(
    private val activityRepository: IActivityRepository
) : ViewModel() {

    // Variables to show nav bars and floating button
    var showAddButton by mutableStateOf(true)
    var showNavBars by mutableStateOf(true)

    // Variables to store activities to show and edit
    var activityToShow: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToEdit: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToDelete by mutableStateOf<SportActivity?>(null)


    /* ############################################################################################# */
    /* ######################### INTERACTION WITH THE ACTIVITY REPOSITORY ########################## */
    /* ############################################################################################# */

    // Function to get the activity list of a specific type of sport
    fun getActivitiesByType(type: String): Flow<List<SportActivity>> {
        return activityRepository.getActivitiesByType(type)
    }

    // Function to push activities in the DB (MAP DIFF AND SPORT TO ENGLISH BEFORE SAVE)
    suspend fun addActivity(
        routeName: String,
        routeDistance: Double,
        startingPoint: String,
        grade: Double,
        selectedDifficulty: String,
        selectedSport: String
    ) {
        val englishDifficulty = mapToEnglishDifficulty(selectedDifficulty)
        val englishSport = mapToEnglishSport(selectedSport)

        val activity = SportActivity(
            generateRandomId(routeName),
            routeName,
            routeDistance,
            startingPoint,
            grade,
            englishDifficulty,
            englishSport
        )
        activityRepository.addActivity(activity)
    }

    // Function to remove an activity from the DB
    suspend fun deleteActivity(activity: SportActivity){
        activityRepository.deleteActivity(activity)
    }

    // Function to update an activity and save it on the DB (MAP DIFF AND SPORT TO ENGLISH BEFORE SAVE)
    suspend fun updateActivity(
        id: Long,
        routeName: String,
        routeDistance: Double,
        startingPoint: String,
        grade: Double,
        selectedDifficulty: String,
        selectedSport: String
    ){
        val englishDifficulty = mapToEnglishDifficulty(selectedDifficulty)
        val englishSport = mapToEnglishSport(selectedSport)

        val updatedActivity = SportActivity(id, routeName, routeDistance, startingPoint, grade, englishDifficulty, englishSport)
        activityRepository.updateActivity(updatedActivity)
    }


}