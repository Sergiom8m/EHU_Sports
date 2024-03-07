package com.example.menditrack.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.menditrack.R
import com.example.menditrack.model.SportActivity
import java.io.File
import java.io.FileWriter
import kotlin.random.Random

// Function to export an activity in txt
fun exportActivityToTxt(activity: SportActivity) {
    val ExternalStorageState = Environment.getExternalStorageState()
    if (ExternalStorageState == Environment.MEDIA_MOUNTED) {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDir, "${activity.name}.txt")


        FileWriter(file).use { writer ->
            with(writer) {
                append("NAME: ${activity.name}\n\n")
                append("DISTANCE: ${activity.distance} km\n")
                append("START POINT: ${activity.initPoint}\n")
                append("GRADE: ${activity.grade} m\n")
                append("DIFFICULTY: ${activity.difficulty}\n")
            }
        }
    }
}

// Function to open Google Maps in an exact location using intents
fun openGoogleMaps(startPoint: String, context: Context) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=$startPoint")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    ContextCompat.startActivity(context, mapIntent, null)
}

// Function to generate a random id long based on the activity name
fun generateRandomId(name: String): Long {
    val hash = name.hashCode()

    val random = Random(hash.toLong())

    return random.nextLong()
}

// Function to map selected difficulty to english for storage in DB (From UI to DB)
fun mapToEnglishDifficulty(selectedDifficulty: String): String {
    return when (selectedDifficulty.toLowerCase()) {
        "fácil", "erraza", "easy" -> "Easy"
        "moderado", "ertaina", "moderate" -> "Moderate"
        "difícil", "zaila", "hard" -> "Hard"
        else -> selectedDifficulty
    }
}

// Function to map selected sport to english for storage in DB (From UI to DB)
fun mapToEnglishSport(selectedSport: String): String {
    return when (selectedSport.toLowerCase()) {
        "ciclismo", "bizikleta", "cycling" -> "Cycling"
        "carrera", "korrika", "running" -> "Running"
        "caminata", "ibilaldia", "walking" -> "Walking"
        else -> selectedSport
    }
}

// Function to map difficulty to user's selected language (From DB to UI)
@Composable
fun mapToUserLanguageDifficulty(englishDifficulty: String): String {
    return when (englishDifficulty.toLowerCase()) {
        "easy" -> stringResource(id = R.string.easy)
        "moderate" -> stringResource(id = R.string.moderate)
        "hard" -> stringResource(id = R.string.hard)
        else -> englishDifficulty
    }
}

// Function to map sport to user's selected language (From DB to UI)
@Composable
fun mapToUserLanguageSport(englishSport: String): String {
    return when (englishSport.toLowerCase()) {
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
    return totalWalkingActivities.sumByDouble { it.distance }.toInt()
}