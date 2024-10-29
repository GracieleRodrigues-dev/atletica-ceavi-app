package com.example.atletica_ceavi_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.model.Training
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.TrainingViewModel

@Composable
fun TrainingListPage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val trainingViewModel: TrainingViewModel = viewModel()
    val trainings by trainingViewModel.training.collectAsState(initial = emptyList())
    val scrollState = rememberScrollState()

    DrawerLayout(navController, authViewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (trainings.isEmpty()) {
                Text("Nenhum treino encontrado.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(trainings) { training ->
                        TrainingItem(training = training)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = {
                        trainingViewModel.refreshTrainings()
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Atualizar Treinos")
                }

                FloatingActionButton(
                    onClick = {
                        navController.navigate("newTraining")
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Novo Treino")
                }
            }
        }
    }
}

@Composable
fun TrainingItem(training: Training) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Data: ${training.date}")
                Text(text = "Hora: ${training.time}")
                Text(text = "Local: ${training.locationName}")

                training.notes?.let { Text(text = "Notas: $it") }

                training.teams?.let { teams ->
                    if (teams.isNotEmpty()) {
                        Text(text = "Equipes:", style = MaterialTheme.typography.bodyLarge)
                        teams.forEach { team ->
                            Text(text = team.toString())
                        }
                    }
                }
            }
        }
    }
}
