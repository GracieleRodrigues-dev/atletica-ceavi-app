package com.example.atletica_ceavi_app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthState
import com.example.atletica_ceavi_app.viewModel.AuthViewModel


@Composable
fun UserRegistrationPage(
    navController: NavController,
    authViewModel: AuthViewModel,
    onUserRegistered: () -> Unit
) {
    DrawerLayout(navController, authViewModel)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val genders = listOf("Masculino", "Feminino", "Outro")
    var selectedGender by remember { mutableStateOf(genders[0]) }
    var genderMenuExpanded by remember { mutableStateOf(false) }

    val isDropDownGenderExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    val roles = listOf("Administrador", "Treinador", "Atleta")
    var selectedRole by remember { mutableStateOf(roles[0]) }
    var roleMenuExpanded by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Idade") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            OutlinedTextField(
                value = selectedGender,
                onValueChange = {},
                label = { Text("Gênero") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { genderMenuExpanded = true },
                readOnly = true
            )
            DropdownMenu(
                expanded = isDropDownGenderExpanded.value,
                onDismissRequest = { isDropDownGenderExpanded.value = false }
            ) {
                genders.forEachIndexed { index, gender ->
                    DropdownMenuItem(text = {
                        Text(text = gender)
                    },
                        onClick = {
                            isDropDownGenderExpanded.value = false
                            itemPosition.value = index
                        })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                label = { Text("Função") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { roleMenuExpanded = true },
                readOnly = true
            )
            DropdownMenu(
                expanded = roleMenuExpanded,
                onDismissRequest = { roleMenuExpanded = false }
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(onClick = {
                        selectedRole = role
                        roleMenuExpanded = false
                    }, text = { Text(text = role) })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val ageInt = age.toIntOrNull()
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && ageInt != null) {
                authViewModel.signup(email, password)
            }
        }) {
            Text("Cadastrar")
        }

        when (authState) {
            is AuthState.Authenticated -> {
                Text("Usuário cadastrado com sucesso!")
                LaunchedEffect(Unit) {
                    onUserRegistered()
                }
            }
            is AuthState.Error -> {
                Text("Erro: ${(authState as AuthState.Error).message}")
            }
            is AuthState.Loading -> {
                Text("Carregando...")
            }
            else -> {}
        }
    }
}
