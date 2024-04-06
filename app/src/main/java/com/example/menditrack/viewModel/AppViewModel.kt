package com.example.menditrack.viewModel


import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menditrack.model.IActivityRepository
import com.example.menditrack.data.SportActivity
import com.example.menditrack.data.User
import com.example.menditrack.model.IUserRepository
import com.example.menditrack.utils.generateRandomId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.menditrack.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val activityRepository: IActivityRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    init {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                addUsersFromRemote()
                addActivitiesFromRemote()
            } catch (_: Exception) {

            }
        }
    }

    // Variables to show nav bars and floating button
    var showAddButton by mutableStateOf(true)
    var showNavBars by mutableStateOf(true)

    // Variables to manage dialog visibility
    var showInfo by mutableStateOf(false)
    var showSettings by mutableStateOf(false)
    var showThemes by mutableStateOf(false)
    var showDelete by mutableStateOf(false)

    // Variables to store activities to show and edit
    var activityToShow: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToEdit: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToDelete by mutableStateOf<SportActivity?>(null)

    var actualUser: MutableState<User> = mutableStateOf(User("",""))

    var profilePicture: Bitmap? by mutableStateOf(null)

    var profilePicturePath: String? = null


    /* ############################################################################################# */
    /* ######################### INTERACTION WITH THE ACTIVITY REPOSITORY ########################## */
    /* ############################################################################################# */

    // Function to get the activity list of a specific type of sport
    fun getActivitiesByType(type: String, username:String): Flow<List<SportActivity>> {
        return activityRepository.getActivitiesByType(type, username)
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
            englishSport,
            actualUser.value.username
        )
        activityRepository.addActivity(activity)
    }

    // Function to remove an activity from the DB
    suspend fun deleteActivity(activity: SportActivity){
        activityRepository.deleteActivity(activity)
    }

    // Function to update an activity and save it on the DB (MAP DIFF AND SPORT TO ENGLISH BEFORE SAVE)
    suspend fun updateActivity(
        id: String,
        routeName: String,
        routeDistance: Double,
        startingPoint: String,
        grade: Double,
        selectedDifficulty: String,
        selectedSport: String
    ){
        val englishDifficulty = mapToEnglishDifficulty(selectedDifficulty)
        val englishSport = mapToEnglishSport(selectedSport)

        val updatedActivity = SportActivity(id, routeName, routeDistance, startingPoint, grade, englishDifficulty, englishSport, actualUser.value.username)
        activityRepository.updateActivity(updatedActivity)
    }

    suspend fun addActivitiesFromRemote() {
        activityRepository.addActivitiesFromRemote()
    }

    /* ############################################################################################# */
    /* ########################## INTERACTION WITH THE USERS REPOSITORY ############################ */
    /* ############################################################################################# */

    suspend fun addUser(
        username: String,
        password: String
    ):Boolean {
        val newUser = User(username, hashPassword(password))
        val existingUser = userRepository.getUser(username)
        if (existingUser == null) {
            userRepository.addUser(newUser)
            return true
        }
        else{
            return false
        }
    }

    suspend fun getUser(username: String): User? {
        return userRepository.getUser(username)
    }

    suspend fun addUsersFromRemote(){
        userRepository.addUsersFromRemote()
    }

    fun setProfileImage(username: String, image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.setUserProfile(username, image)
            profilePicture = image
        }

    }

    fun getProfileImage(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            profilePicture = userRepository.getUserProfile(username)
        }
    }


}

