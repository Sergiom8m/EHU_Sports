package com.example.menditrack.backgroundServ

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.menditrack.MainActivity
import com.example.menditrack.R

// BroadcastReceiver to handle alarm events
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        // Retrieve title and body from the intent
        val title = intent?.getStringExtra("TITLE") ?: return
        val body = intent.getStringExtra("BODY") ?: return

        // Generate a unique notification ID
        val notificationId = System.currentTimeMillis().toInt()

        // Create a notification using NotificationCompat.Builder
        val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.applogo) // Set the small icon
            .setContentTitle(title) // Set the title
            .setContentText(body) // Set the body text
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority to high
            .setAutoCancel(true) // Automatically cancel the notification when tapped

        // Check if the app has the required permission to post notifications
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Post the notification
                notify(notificationId, builder.build())
            }
        }
    }
}