package com.thakurnitin2684.mytasks

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat


class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, "myChannelId")
        val bndl = intent?.extras
        val taskName = bndl?.get(TASK_NAME)
        builder.setContentTitle(taskName as CharSequence?)
//        builder.setContentText("Task title")
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT

        notificationManager.notify(getCurrentNotificationId(context.applicationContext), builder.build())
    }
    fun getCurrentNotificationId(iContext: Context?): Int {
         val NOTIFICATION_ID_UPPER_LIMIT = 30000 // Arbitrary number.
        val NOTIFICATION_ID_LOWER_LIMIT = 0
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(iContext)
        val previousTokenId = sharedPreferences.getInt("currentNotificationTokenId", 0)
        val currentTokenId = previousTokenId + 1
        val editor = sharedPreferences.edit()
        if (currentTokenId < NOTIFICATION_ID_UPPER_LIMIT) {
            editor.putInt("currentNotificationTokenId", currentTokenId) // }
        } else {
            //If reaches the limit reset to lower limit..
            editor.putInt("currentNotificationTokenId", NOTIFICATION_ID_LOWER_LIMIT)
        }
        editor.commit()
        return currentTokenId
    }
    companion object {
        var TASK_NAME = "tn"
//        var NOTIFICATION = "notification"
    }
}