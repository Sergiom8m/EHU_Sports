package com.example.menditrack.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Environment
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
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
import android.provider.CalendarContract.Events
import android.util.Log
import java.util.TimeZone

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

// Function to get latitude and longitude coordinates from a given address (String)
fun getLatLngFromAddress(context: Context, mAddress: String): Pair<Double, Double>? {
    val coder = Geocoder(context)
    try {
        val addressList = coder.getFromLocationName(mAddress, 1)
        if (addressList.isNullOrEmpty()) {
            return null
        }
        val location = addressList[0]
        return Pair(location.latitude, location.longitude)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}


// Function to transform local user data class to remote user data class
fun userToPostUser(user: User): PostUser {
    return PostUser(
        user.username,
        user.password
    )
}

// Function to transform remote user data class to local user data class
fun postUserToUser(postUser: PostUser): User {
    return User(
        postUser.username,
        postUser.password
    )
}

// Function to transform local activity data class to remote activity data class
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

// Function to transform remote activity data class to local activity data class
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

// Function to print a flickering image given the image and the size
@Composable
fun FlickeringImage(image: Int, size: Dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Image(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .alpha(alpha),
        painter = painterResource(id = image),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

// Function to get all calendar's IDs (including google calendars)
@SuppressLint("Range")
fun getAllCalendarIds(context: Context): List<Long> {
    val calendarIds = mutableListOf<Long>()
    val projection = arrayOf(CalendarContract.Calendars._ID)

    val selection = "${CalendarContract.Calendars.ACCOUNT_TYPE} = ? AND ${CalendarContract.Calendars.NAME} LIKE ?"
    val selectionArgs = arrayOf("com.google", "%@%")

    context.contentResolver.query(
        CalendarContract.Calendars.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID))
            calendarIds.add(id)
        }
    }
    return calendarIds
}

// Function to get the local calendar's IDs (except google calendars)
@SuppressLint("Range")
fun getLocalCalendarIds(context: Context): List<Long> {
    val calendarIds = mutableListOf<Long>()
    val projection = arrayOf(CalendarContract.Calendars._ID)

    // Filter the calendars in order to keep only the local ones
    val selection = "${CalendarContract.Calendars.ACCOUNT_TYPE} IN (?)"
    val selectionArgs = arrayOf("LOCAL")

    context.contentResolver.query(
        CalendarContract.Calendars.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID))
            calendarIds.add(id)
        }
    }
    return calendarIds
}


// Function to add future activities to calendars
@RequiresApi(Build.VERSION_CODES.O)
fun addEventOnCalendar(context: Context, title: String, dateP: Long){
    val contentResolver: ContentResolver = context.contentResolver
    val timeZone = TimeZone.getDefault().id

    // If is possible add events on local calendar
    var calendarIDs = getLocalCalendarIds(context)

    // If not local calendars available try to add events in other calendars
    if (calendarIDs.isEmpty()){
        Log.d("NO LOCALS", "NO LOCALS")
        calendarIDs = getAllCalendarIds(context)
    }

    for (calendarID in calendarIDs) {

        val contentValues = ContentValues().apply {
            put(Events.CALENDAR_ID, calendarID)
            put(Events.TITLE, title)
            put(Events.DTSTART, dateP)
            put(Events.DTEND, dateP +  (24 * 60 * 60 * 1000))
            put(Events.ALL_DAY, 1)
            put(Events.EVENT_TIMEZONE, timeZone)
        }
        contentResolver.insert(Events.CONTENT_URI, contentValues)
    }
}

// Function to get all calendar's IDs (including google calendars) and print their attributes
@SuppressLint("Range")
fun printCalendarAttributes(context: Context) {
    val projection = arrayOf(
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.NAME,
        // Add more attributes as needed
    )

    context.contentResolver.query(
        CalendarContract.Calendars.CONTENT_URI,
        projection,
        null,
        null,
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID))
            val name = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.NAME))

            // Print the attributes of each calendar
            Log.d("Calendar Attributes", "ID: $id, Name: $name")
        }
    }
}