package com.example.waterreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.waterreminder.ui.theme.WaterReminderTheme
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WaterReminderTheme {
                ReminderScreen()
            }
        }
    }
}

@Composable
private fun ReminderScreen() {
    val context = LocalContext.current
    val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    var statusMessage by remember { mutableStateOf("Noch keine Erinnerung aktiv") }
    var interval by remember { mutableFloatStateOf((ReminderPreferences.getInterval(context) ?: 2L).toFloat()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        statusMessage = if (granted) {
            "Berechtigung erlaubt. Du kannst Erinnerungen starten."
        } else {
            "Benachrichtigungs-Berechtigung verweigert."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Wasser-Erinnerung",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Intervall: ${interval.roundToLong()} Stunden")

        Slider(
            value = interval,
            onValueChange = { interval = it },
            valueRange = 1f..8f,
            steps = 6
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                val roundedHours = interval.roundToLong().coerceAtLeast(1L)
                ReminderScheduler.schedule(context, roundedHours)
                ReminderPreferences.saveInterval(context, roundedHours)
                statusMessage = "Erinnerung alle $roundedHours Stunde(n) aktiviert."
            }
        }) {
            Text("Erinnerung starten")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            ReminderScheduler.cancel(context)
            ReminderPreferences.clear(context)
            statusMessage = "Erinnerung gestoppt"
        }) {
            Text("Erinnerung stoppen")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = statusMessage)
    }
}
