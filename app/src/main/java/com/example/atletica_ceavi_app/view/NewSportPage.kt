package com.example.atletica_ceavi_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.SportViewModel

@Composable
fun NewSportPage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val sportViewModel: SportViewModel = viewModel()
    DrawerLayout(navController, authViewModel) {

        var sportName by remember { mutableStateOf("") }
        var sportDescription by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = sportName,
                onValueChange = { sportName = it },
                label = { Text("Nome da Modalidade") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sportDescription,
                onValueChange = { sportDescription = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (sportName.isNotEmpty()) {
                    sportViewModel.addSport(sportName, sportDescription)
                }
            }) {
                Text("Cadastrar Modalidade")
            }
        }
    }
}
