package com.example.menditrack.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
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
import java.time.LocalDate
import java.time.ZoneOffset
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

@Composable
fun LoadingImagePlaceholder(image: Int, size: Dp) {
    // Creates an `InfiniteTransition` that runs infinite child animation values.
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        // `infiniteRepeatable` repeats the specified duration-based `AnimationSpec` infinitely.
        animationSpec = infiniteRepeatable(
            // The `keyframes` animates the value by specifying multiple timestamps.
            animation = keyframes {
                // One iteration is 1000 milliseconds.
                durationMillis = 1000
                // 0.7f at the middle of an iteration.
                0.7f at 500
            },
            // When the value finishes animating from 0f to 1f, it repeats by reversing the
            // animation direction.
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

@SuppressLint("Range")
fun getCalendarIds(context: Context): List<Long> {
    val calendarIds = mutableListOf<Long>()
    val projection = arrayOf(CalendarContract.Calendars._ID)

    val selection = "${CalendarContract.Calendars.ACCOUNT_TYPE} NOT IN (?)"
    val selectionArgs = arrayOf("com.google")

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

@RequiresApi(Build.VERSION_CODES.O)
fun addEventOnCalendar(context: Context, title: String, dateP: Long){

    val contentResolver: ContentResolver = context.contentResolver

    val timeZone = TimeZone.getDefault().id

    val calendarIDs = getCalendarIds(context)

    for (calendarID in calendarIDs) {

        val contentValues = ContentValues().apply {
            put(Events.CALENDAR_ID, calendarID)
            put(Events.TITLE, title)
            put(Events.DTSTART, dateP)
            put(Events.DTEND, dateP +  (24 * 60 * 60 * 1000))
            put(Events.ALL_DAY, 1)
            put(Events.EVENT_TIMEZONE, timeZone)
        }

        val uri = contentResolver.insert(Events.CONTENT_URI, contentValues)

        if (uri != null) {
            val eventId = ContentUris.parseId(uri)
            addEventReminder(context, eventId, 720)
        } else {
            Log.d("CALENDAR", "INCORRECTO")
        }
    }

}

fun addEventReminder(context: Context, eventId: Long, timePrev: Int) {
    val contentResolver: ContentResolver = context.contentResolver

    val reminderValues = ContentValues().apply {
        put(CalendarContract.Reminders.EVENT_ID, eventId)
        put(CalendarContract.Reminders.MINUTES, timePrev)
        put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
    }

    contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)

}
