package com.example.menditrack.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.example.menditrack.R
import com.example.menditrack.data.SportActivity
import com.example.menditrack.data.User
import com.example.menditrack.remote.PostActivity
import com.example.menditrack.remote.PostUser
import java.io.File
import java.io.FileWriter
import java.security.MessageDigest
import java.util.Locale
import kotlin.random.Random

// Function to convert SportActivity to string (data extraction)
fun activityToString(activity: SportActivity): String{
    return "NAME: ${activity.name}\n" +
            "\n" +
            "DISTANCE: ${activity.distance} km\n" +
            "START POINT: ${activity.initPoint}\n" +
            "GRADE: ${activity.grade} m\n" +
            "DIFFICULTY: ${activity.difficulty}"
}

// Function to export an activity in txt
fun exportActivityToTxt(activity: SportActivity) {
    val externalStorageState = Environment.getExternalStorageState()
    if (externalStorageState == Environment.MEDIA_MOUNTED) {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDir, "${activity.name}.txt")

        FileWriter(file).use { writer ->
            with(writer) {
                append(activityToString(activity))
            }
        }
    }
}

// Function to open Google Maps in an exact location using intents
fun openGoogleMaps(startPoint: String, context: Context) {

    try {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$startPoint")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(context, mapIntent, null)
    } catch (_: Exception) {

    }
}

// Function to open Gmail with a default subject, body and destination
fun openEmail(context: Context) {
    val emailAddress = "menditrackteam@gmail.com"

    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        intent.setPackage("com.google.android.gm")
        startActivity(context, intent, null)
    } catch (_: Exception) {

    }
}

// Function to open whatsapp in order to share a text with the activity's information
fun openShare(activity: SportActivity, context: Context){
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, activityToString(activity))
        startActivity(context, intent, null)
    } catch (_: Exception) {

    }
}

// Function to generate a random id long based on the activity name
fun generateRandomId(name: String): String {
    val hash = name.hashCode()
    val random = Random(hash.toLong())
    return random.nextLong().toString()
}

// Function to map selected difficulty to english for storage in DB (From UI to DB)
fun mapToEnglishDifficulty(selectedDifficulty: String): String {
    return when (selectedDifficulty.lowercase(Locale.ROOT)) {
        "fácil", "erraza", "easy" -> "Easy"
        "moderado", "ertaina", "moderate" -> "Moderate"
        "difícil", "zaila", "hard" -> "Hard"
        else -> selectedDifficulty
    }
}

// Function to map selected sport to english for storage in DB (From UI to DB)
fun mapToEnglishSport(selectedSport: String): String {
    return when (selectedSport.lowercase(Locale.ROOT)) {
        "ciclismo", "bizikleta", "cycling" -> "Cycling"
        "carrera", "korrika", "running" -> "Running"
        "caminata", "ibilaldia", "walking" -> "Walking"
        else -> selectedSport
    }
}

// Function to map difficulty to user's selected language (From DB to UI)
@Composable
fun mapToUserLanguageDifficulty(englishDifficulty: String): String {
    return when (englishDifficulty.lowercase(Locale.ROOT)) {
        "easy" -> stringResource(id = R.string.easy)
        "moderate" -> stringResource(id = R.string.moderate)
        "hard" -> stringResource(id = R.string.hard)
        else -> englishDifficulty
    }
}

// Function to map sport to user's selected language (From DB to UI)
@Composable
fun mapToUserLanguageSport(englishSport: String): String {
    return when (englishSport.lowercase(Locale.ROOT)) {
        "running" -> stringResource(id = R.string.running)
        "walking" -> stringResource(id = R.string.walking)
        "cycling" -> stringResource(id = R.string.cycling)
        else -> englishSport
    }
}

// Function to test the validity of the data fields content
fun isValidInput(
    name: String,
    distance: String,
    point: String,
    grade: String,
    diff: String,
    sport: String): Boolean
{
    val validName = name.isNotBlank()
    val validDistance = distance.toDoubleOrNull() != null && distance.toDouble() > 0
    val validPoint = point.isNotBlank()
    val validGrade = grade.toDoubleOrNull() != null && grade.toDouble() > 0
    val validDiff = diff.isNotBlank()
    val validSport = sport.isNotBlank()

    return validName && validDistance && validPoint && validGrade && validDiff && validSport
}

// Function to sum the distances of activity list
fun sumActivityDistances(totalWalkingActivities: List<SportActivity>): Int {
    return totalWalkingActivities.sumOf { it.distance }.toInt()
}

// Function to hash passwords using SHA-256
fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashedBytes = digest.digest(password.toByteArray())
    return hashedBytes.joinToString("") { "%02x".format(it) }
}

fun userToPostUser(user: User): PostUser {
    return PostUser(
        user.username,
        user.password
    )
}

fun postUserToUser(postUser: PostUser): User {
    return User(
        postUser.username,
        postUser.password
    )
}

fun activityToPostActivity(activity: SportActivity): PostActivity {
    return PostActivity(
        activity.id,
        activity.name,
        activity.distance,
        activity.initPoint,
        activity.grade,
        activity.difficulty,
        activity.type,
        activity.userId
    )
}

fun postActivityToActivity(postActivity: PostActivity): SportActivity {
    return SportActivity(
        postActivity.id,
        postActivity.name,
        postActivity.distance,
        postActivity.initPoint,
        postActivity.grade,
        postActivity.difficulty,
        postActivity.type,
        postActivity.user_id
    )
}