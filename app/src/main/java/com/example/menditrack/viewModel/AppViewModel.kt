package com.example.menditrack.viewModel

import android.graphics.Bitmap
import android.util.Log
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

    // Variable to detect the correct download of remote data
    var correctInit by mutableStateOf(false)

    // Variables to show nav bars and floating button
    var showAddButton by mutableStateOf(true)
    var showNavBars by mutableStateOf(true)

    // Variables to manage dialog visibility
    var showSettings by mutableStateOf(false)
    var showThemes by mutableStateOf(false)
    var showDelete by mutableStateOf(false)

    // Variables to store activities to show and edit
    var activityToShow: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToEdit: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToDelete by mutableStateOf<SportActivity?>(null)

    // Variable to store the current logged user's username and password
    var actualUser: MutableState<User> = mutableStateOf(User("",""))

    // Variable to store the current user's profile picture
    var profilePicture: Bitmap? by mutableStateOf(null)


    /* ############################################################################################# */
    /* ############################ INTERACTION WITH THE REMOTE SERVER ############################# */
    /* ############################################################################################# */

    // Function to get all the data from the remote DB and insert it into local RoomDB
    fun downloadData(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                addUsersFromRemote()
                addActivitiesFromRemote()
                correctInit = true
            } catch (_: Exception) { }
        }
    }

    // Function to register device tokens on the remote DB (for Firebase Cloud Messaging)
    fun subscribeDevice(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userRepository.subscribe(token)
                Log.d("TOKEN CORRECT", "TOKEN CORRECT")
            }
            catch (_:Exception){ }
        }
    }

    private suspend fun addActivitiesFromRemote() {
        activityRepository.addActivitiesFromRemote()
    }

    private suspend fun addUsersFromRemote(){
        userRepository.addUsersFromRemote()
    }

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

    /* ############################################################################################# */
    /* ########################## INTERACTION WITH THE USERS REPOSITORY ############################ */
    /* ############################################################################################# */

    // Function to add new users
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

    // Function to obtain an specific user given his username
    suspend fun getUser(username: String): User? {
        return userRepository.getUser(username)
    }

    // Function to set profile image
    fun setProfileImage(username: String, image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.setUserProfile(username, image)
            profilePicture = image
        }
    }

    // Function to get profile image
    fun getProfileImage(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            profilePicture = userRepository.getUserProfile(username)
        }
    }
}