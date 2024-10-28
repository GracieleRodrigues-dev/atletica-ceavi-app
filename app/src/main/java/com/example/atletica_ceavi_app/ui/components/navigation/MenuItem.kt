package com.example.atletica_ceavi_app.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.atletica_ceavi_app.ui.components.icons.Alarm_on
import com.example.atletica_ceavi_app.ui.components.icons.People
import com.example.atletica_ceavi_app.ui.components.icons.Sports_baseball

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
    ),
    MenuItem("Modalidades",
        "",
        Sports_baseball,
        subItems = listOf(
            MenuItem("Lista de Modalidades", "sports", Sports_baseball),
            MenuItem("Adicionar Modalidade", "newSport", Icons.Filled.AddCircle)
        )),
    MenuItem("Equipes",
        "",
        People,
        subItems = listOf(
            MenuItem("Lista de Equipes", "teams", People),
            MenuItem("Adicionar Equipe", "newTeam", Icons.Filled.AddCircle)
        )),

    MenuItem("Adicionar Treino", "newTraining", Alarm_on) ,
)