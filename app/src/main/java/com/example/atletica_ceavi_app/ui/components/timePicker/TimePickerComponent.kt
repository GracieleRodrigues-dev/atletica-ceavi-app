package com.example.atletica_ceavi_app.ui.components.timePicker

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.atletica_ceavi_app.ui.components.icons.Clock
import java.util.*

@Composable
fun TimePickerComponent(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    labelText: String = "Hora"
) {
    var showTimePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedTime,
        onValueChange = {},
        label = { Text(labelText) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                Icon(imageVector = Clock, contentDescription = "Selecionar Hora")
            }
        }
    )

    if (showTimePicker) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                onTimeSelected(time)
                showTimePicker = false
            },
            hour,
            minute,
            true
        ).show()
    }
}
