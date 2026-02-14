# Water Reminder (Android, Kotlin)

Eine einfache Android-App in Kotlin (Jetpack Compose), die dich regelmäßig daran erinnert, Wasser zu trinken.

## Features
- Intervall zwischen 1 und 8 Stunden auswählbar.
- Starten/Stoppen der Erinnerung direkt in der App.
- Lokale Benachrichtigungen via `WorkManager`.
- Nach Neustart des Geräts werden aktive Erinnerungen automatisch wiederhergestellt.

## Technischer Aufbau
- **UI:** Jetpack Compose (`MainActivity`)
- **Hintergrundjobs:** `WorkManager` (`WaterReminderWorker`)
- **Persistenz:** `SharedPreferences` (`ReminderPreferences`)
- **Neustart-Handling:** `BootCompletedReceiver`

## Ausführen
1. Projekt in Android Studio öffnen.
2. Gradle-Sync ausführen.
3. Auf einem Android-Gerät oder Emulator starten.
4. Bei Android 13+ die Benachrichtigungs-Berechtigung erlauben.

## Hinweis
Die kleinste zuverlässige Wiederholung bei `PeriodicWorkRequest` ist 15 Minuten, in dieser App aus UX-Gründen als Stundenintervall (1-8h) umgesetzt.
