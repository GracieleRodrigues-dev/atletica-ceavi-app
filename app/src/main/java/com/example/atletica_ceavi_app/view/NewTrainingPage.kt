package com.example.atletica_ceavi_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.model.Team
import com.example.atletica_ceavi_app.ui.components.maps.MapComponent
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.TrainingViewModel
import com.google.android.gms.maps.model.LatLng

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

    val sports by trainingViewModel.sports.collectAsState(emptyList())
    val teams by trainingViewModel.teams.collectAsState(emptyList())
    val selectedSport by trainingViewModel.sport.collectAsState()
    val selectedTeams by trainingViewModel.teams.collectAsState()

    var locationName by remember { mutableStateOf("") }
    var mapPosition by remember { mutableStateOf(LatLng(-23.5505, -46.6333)) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Criar Treino", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Data") }
        )

        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Hora") }
        )

        Button(onClick = { showMapDialog = true }) {
            Text(text = if (locationName.isBlank()) "Selecionar Local" else locationName)
        }

        if (showMapDialog) {
            MapSelectionDialog(
                onLocationSelected = { name, latLng ->
                    locationName = name
                    mapPosition = latLng
                    trainingViewModel.updateSelectedLocation(latLng.latitude, latLng.longitude)
                    showMapDialog = false
                },
                onDismiss = { showMapDialog = false }
            )
        }

        DropdownSelector(
            items = sports,
            selectedItem = selectedSport,
            onItemSelected = { trainingViewModel.updateSport(it) },
            label = "Modalidade"
        )

        TeamSelector(teams, selectedTeams) { updatedTeams ->
            trainingViewModel.updateTeams(updatedTeams)
        }

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
                items = listOf(1, 2, 3, 4, 5, 6, 7),
                selectedItem = recurringDayOfWeek,
                onItemSelected = { day ->
                    recurringDayOfWeek = day
                    trainingViewModel.updateRecurringDayOfWeek(day)
                },
                label = "Dia da Semana"
            )
        }

        Button(
            onClick = {
                trainingViewModel.addTraining()
                navController.popBackStack()
            },
            enabled = date.isNotBlank() && time.isNotBlank() && locationName.isNotBlank() && selectedSport != null
        ) {
            Text("Criar Treino")
        }
    }
}

@Composable
fun TeamSelector(teams: List<Team>, selectedTeams: List<Team>, onTeamsUpdated: (List<Team>) -> Unit) {
    Text("Selecionar Equipes")
    LazyColumn {
        items(teams) { team ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedTeams.contains(team),
                    onCheckedChange = {
                        val updatedTeams = if (it) selectedTeams + team else selectedTeams - team
                        onTeamsUpdated(updatedTeams)
                    }
                )
                Text(text = team.name ?: "Equipe não disponível")
            }
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
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedItem?.toString() ?: label)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.toString()) },
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
    var selectedLocation by remember { mutableStateOf(LatLng(-23.5505, -46.6333)) }
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
