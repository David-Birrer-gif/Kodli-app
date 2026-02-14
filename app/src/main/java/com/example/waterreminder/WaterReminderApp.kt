package com.example.waterreminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class WaterReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            WaterReminderWorker.CHANNEL_ID,
            "Wasser-Erinnerungen",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Erinnert dich regelmäßig ans Trinken"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
