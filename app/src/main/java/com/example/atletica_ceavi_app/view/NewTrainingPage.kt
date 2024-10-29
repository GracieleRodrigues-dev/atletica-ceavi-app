package com.example.atletica_ceavi_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.ui.components.datePicker.DatePickerComponent
import com.example.atletica_ceavi_app.ui.components.maps.MapComponent
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.ui.components.timePicker.TimePickerComponent
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.TrainingViewModel
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

@Composable
fun NewTrainingPage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val trainingViewModel: TrainingViewModel = viewModel()
    DrawerLayout(navController, authViewModel) {
        TrainingForm(trainingViewModel, navController)
    }
}

@Composable
fun TrainingForm(
    trainingViewModel: TrainingViewModel,
    navController: NavController
) {
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isRecurring by remember { mutableStateOf(false) }
    var recurringDayOfWeek by remember { mutableStateOf<Int?>(null) }
    var showMapDialog by remember { mutableStateOf(false) }

    val teams by trainingViewModel.teams.collectAsState(emptyList())
    val selectedTeam by trainingViewModel.selectedTeam.collectAsState()
    val selectedTeams by trainingViewModel.selectedTeams.collectAsState(emptyList())
    val scrollState = rememberScrollState()

    var locationName by remember { mutableStateOf("") }
    var mapPosition by remember { mutableStateOf(LatLng(-27.049484059771927, -49.53754011550491)) }
    val minDate = LocalDate.now()
    val maxDate = LocalDate.now().plusYears(100)
    val dayNames = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = "Criar Treino", style = MaterialTheme.typography.titleMedium)

        DatePickerComponent(
            selectedDate = date,
            onDateSelected = {
                date = it
                trainingViewModel.updateDate(it)
            },
            minDate = minDate,
            maxDate = maxDate,
            labelText = "Data do Treino"
        )

        TimePickerComponent(
            selectedTime = time,
            onTimeSelected = {
                time = it
                trainingViewModel.updateTime(it)
            },
            labelText = "Hora do Treino"
        )

        Button(onClick = { showMapDialog = true }) {
            Text(text = if (locationName.isBlank()) "Selecionar Local" else locationName)
        }

        if (showMapDialog) {
            MapSelectionDialog(
                onLocationSelected = { name, latLng ->
                    locationName = name
                    mapPosition = latLng
                    trainingViewModel.updateLocationName(name)
                    trainingViewModel.updateSelectedLocation(latLng.latitude, latLng.longitude)
                    showMapDialog = false
                },
                onDismiss = { showMapDialog = false }
            )
        }

        Text(text = "Equipes Selecionadas", style = MaterialTheme.typography.titleMedium)
        selectedTeams.forEach { team ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = team.toString())

                IconButton(onClick = {
                    trainingViewModel.removeSelectedTeam(team)
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        }

        DropdownSelector(
            items = teams,
            selectedItem = selectedTeam,
            onItemSelected = {
                trainingViewModel.updateSelectedTeam(it)
                trainingViewModel.addSelectedTeam(it)
            },
            label = "Selecionar Equipe"
        )

        OutlinedTextField(
            value = notes,
            onValueChange = {
                notes = it
                trainingViewModel.updateNotes(it)
            },
            label = { Text("Observações") }
        )

        RecurrenceCheckbox(isRecurring) { newValue ->
            isRecurring = newValue
            trainingViewModel.setIsRecurring(newValue)
        }

        if (isRecurring) {
            DropdownSelector(
                items = (1..7).toList(),
                selectedItem = recurringDayOfWeek,
                onItemSelected = { day ->
                    recurringDayOfWeek = day
                    trainingViewModel.updateRecurringDayOfWeek(day)
                },
                label = "Dia da Semana",
                getDisplayText = { day -> "${day}: ${dayNames[day - 1]}" }
            )
        }

        Button(
            onClick = {
                trainingViewModel.addTraining()
                navController.popBackStack()
            },
            enabled = date.isNotBlank() && time.isNotBlank() && locationName.isNotBlank()
        ) {
            Text("Criar Treino")
        }
    }
}

@Composable
fun RecurrenceCheckbox(isRecurring: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isRecurring,
            onCheckedChange = onCheckedChange
        )
        Text("Treino Recorrente")
    }
}

@Composable
fun <T> DropdownSelector(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    label: String,
    getDisplayText: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedItem?.let { getDisplayText(it) } ?: label)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = getDisplayText(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MapSelectionDialog(onLocationSelected: (String, LatLng) -> Unit, onDismiss: () -> Unit) {
    var selectedLocation by remember { mutableStateOf(LatLng(-27.049398062466928, -49.5374972001611)) }
    var locationName by remember { mutableStateOf("Selecionar Local") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Selecione um Local") },
        text = {
            Column {
                MapComponent(
                    locationName = locationName,
                    mapPosition = selectedLocation,
                    onMapClick = { latLng,address ->
                        selectedLocation = latLng
                        locationName = address
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLocationSelected(locationName, selectedLocation)
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
