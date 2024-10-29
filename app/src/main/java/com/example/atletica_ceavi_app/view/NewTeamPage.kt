package com.example.atletica_ceavi_app.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.TeamViewModel

@Composable
fun NewTeamPage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val teamViewModel: TeamViewModel = viewModel()

    DrawerLayout(navController, authViewModel) {
        var teamName by remember { mutableStateOf("") }
        val sports by teamViewModel.sports.collectAsState()
        val users by teamViewModel.users.collectAsState()
        val selectedSport by teamViewModel.selectedSport.collectAsState()
        val selectedCoach by teamViewModel.selectedCoach.collectAsState()
        val selectedAthletes by teamViewModel.selectedAthletes.collectAsState()
        val isTeamAdded by teamViewModel.isTeamAdded
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Text(text = "Criar Equipe", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text("Nome da Equipe") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Selecione a Modalidade")
            DropdownSelector(
                items = sports,
                selectedItem = selectedSport,
                onItemSelected = { teamViewModel.selectSport(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Selecione o Treinador")
            DropdownSelector(
                items = users.filter { it.role == "Treinador" },
                selectedItem = selectedCoach,
                onItemSelected = { teamViewModel.selectCoach(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Selecione os Atletas")
            LazyColumn {
                items(users.filter { it.role == "Atleta" }) { atleta ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedAthletes.contains(atleta),
                            onCheckedChange = { teamViewModel.toggleAthleteSelection(atleta) }
                        )
                        Text(text = atleta.name ?: "Nome não disponível")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    teamViewModel.createTeam(teamName, selectedSport, selectedCoach, selectedAthletes)
                },
                enabled = teamName.isNotBlank() && selectedSport != null && selectedCoach != null
            ) {
                Text("Criar Equipe")
            }

            LaunchedEffect(isTeamAdded) {
                if (isTeamAdded) {
                    Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                    navController.navigate("teams") {
                        popUpTo("NewTeamPage") { inclusive = true }
                    }
                    teamViewModel.resetTeamAdded()
                }
            }
        }
    }
}

@Composable
fun <T> DropdownSelector(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedItem?.toString() ?: "Selecionar")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.toString()) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    })
            }
        }
    }
}
