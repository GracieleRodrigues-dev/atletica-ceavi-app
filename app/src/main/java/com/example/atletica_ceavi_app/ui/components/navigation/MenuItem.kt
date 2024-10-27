package com.example.atletica_ceavi_app.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val title: String,
    val destination: String,
    val icon: ImageVector,
    val subItems: List<MenuItem>? = null
)

val menuItems = listOf(
    MenuItem("Home", "home", Icons.Filled.Home),
    MenuItem("Perfil", "profile", Icons.Filled.AccountCircle),
    MenuItem("Notificações", "notifications", Icons.Filled.Notifications) ,
    MenuItem(
        "Usuários",
        "",
        Icons.Filled.Notifications,
        subItems = listOf(
            MenuItem("Lista de Usuários", "users", Icons.Filled.Face),
            MenuItem("Adicionar Usuário", "userRegistration", Icons.Filled.AddCircle)
        )
    )
)