package com.example.atletica_ceavi_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.ui.components.datePicker.DatePickerComponent
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import java.time.LocalDate

@Composable
fun UserRegistrationPage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Masculino") }
    var selectedRole by remember { mutableStateOf("Atleta") }
    var showSnackbar by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    val genders = listOf("Masculino", "Feminino", "Outro")
    val roles = listOf("Administrador", "Treinador", "Atleta")

    val minDate = LocalDate.now().minusYears(100)
    val maxDate = LocalDate.now()

    DrawerLayout(navController, authViewModel) {
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
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            DatePickerComponent(
                selectedDate = birthDate,
                onDateSelected = { birthDate = it },
                minDate = minDate,
                maxDate = maxDate,
                labelText = "Data de Nascimento"
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownMenuBox(
                items = genders,
                label = "Gênero",
                selectedItem = selectedGender,
                onItemSelected = { selectedGender = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownMenuBox(
                items = roles,
                label = "Papel",
                selectedItem = selectedRole,
                onItemSelected = { selectedRole = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && birthDate.isNotEmpty()) {
                    authViewModel.signup(
                        email = email,
                        password = password,
                        name = name,
                        birthDate = birthDate,
                        gender = selectedGender,
                        role = selectedRole
                    ) { success, message ->
                        feedbackMessage = if (success) {
                            navController.navigate("users")
                            "Cadastro realizado com sucesso!"
                        } else {
                            message ?: "Erro desconhecido."
                        }
                        showSnackbar = true
                    }
                } else {
                    feedbackMessage = "Todos os campos são obrigatórios."
                    showSnackbar = true
                }
            }, modifier = Modifier.align(Alignment.End)) {
                Text("Cadastrar")
            }

            if (showSnackbar) {
                Snackbar(
                    action = {
                        TextButton(onClick = { showSnackbar = false }) {
                            Text("Fechar")
                        }
                    }
                ) {
                    Text(feedbackMessage)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    items: List<String>,
    label: String,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
