package com.example.waterreminder

import android.content.Context

object ReminderPreferences {
    private const val PREFS_NAME = "water_reminder_prefs"
    private const val KEY_INTERVAL = "interval_hours"

    fun saveInterval(context: Context, hours: Long) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putLong(KEY_INTERVAL, hours)
            .apply()
    }

    fun getInterval(context: Context): Long? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.contains(KEY_INTERVAL)) return null
        return prefs.getLong(KEY_INTERVAL, 2L)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_INTERVAL)
            .apply()
    }
}
