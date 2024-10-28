package com.example.atletica_ceavi_app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.UserViewModel

@Composable
fun ProfilePage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userViewModel: UserViewModel = viewModel()

    DrawerLayout(navController, authViewModel) {
        val loggedUser by userViewModel.loggedUser.collectAsState()

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Perfil do Usuário", style = MaterialTheme.typography.titleMedium)

            loggedUser?.let {
                Text(text = "Nome: ${it.name}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Função: ${it.role}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Data de Nascimento: ${it.birthDate}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Gênero: ${it.gender}", style = MaterialTheme.typography.bodyMedium)
            } ?: run {
                Text(text = "Carregando dados do usuário...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
