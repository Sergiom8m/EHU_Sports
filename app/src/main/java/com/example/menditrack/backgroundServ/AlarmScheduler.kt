package com.example.menditrack.backgroundServ

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.menditrack.data.FutureActivity
import java.time.LocalDateTime
import java.time.ZoneId

// Interface defining the contract for scheduling alarms
interface AlarmScheduler {
    // Function to schedule a future activity
    fun schedule(futureActivity: FutureActivity)
}

// Implementation of AlarmScheduler for Android platform
class AndroidAlarmScheduler (
    private val context: Context
): AlarmScheduler {

    // Initialize the AlarmManager instance
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    // Function to schedule a future activity
    @RequiresApi(Build.VERSION_CODES.O)
    override fun schedule(futureActivity: FutureActivity) {
        // Check if the future activity's time is in the future
        if (futureActivity.time > LocalDateTime.now()) {
            // Create an intent to trigger the alarm receiver
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("TITLE", futureActivity.title)
                putExtra("BODY", futureActivity.body)
            }
            // Schedule the alarm using AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                futureActivity.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                PendingIntent.getBroadcast(
                    context,
                    futureActivity.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }
}