package com.example.atletica_ceavi_app.ui.components.datePicker


import android.app.DatePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun DatePickerComponent(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    labelText: String = "Data"
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(labelText) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Selecionar Data")
            }
        }
    )

    if (showDatePicker) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()


        minDate?.let {
            calendar.set(it.year, it.monthValue - 1, it.dayOfMonth)
        }
        val minMillis = minDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli() ?: Long.MIN_VALUE
        val maxMillis = maxDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli() ?: Long.MAX_VALUE

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = LocalDate.of(year, month + 1, dayOfMonth)
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                onDateSelected(formattedDate)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )


        datePickerDialog.datePicker.minDate = minMillis
        datePickerDialog.datePicker.maxDate = maxMillis

        datePickerDialog.show()
    }
}
