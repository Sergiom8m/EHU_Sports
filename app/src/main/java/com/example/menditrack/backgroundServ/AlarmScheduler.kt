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

interface AlarmScheduler {
    fun schedule(futureActivity: FutureActivity)
}
class AndroidAlarmScheduler (
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun schedule(futureActivity: FutureActivity) {
        if (futureActivity.time > LocalDateTime.now()) {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("TITLE", futureActivity.title)
                putExtra("BODY", futureActivity.body)
            }
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