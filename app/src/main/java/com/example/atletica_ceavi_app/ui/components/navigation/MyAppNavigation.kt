package com.example.atletica_ceavi_app.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atletica_ceavi_app.HomePage
import com.example.atletica_ceavi_app.NotificationsPage
import com.example.atletica_ceavi_app.ProfilePage
import com.example.atletica_ceavi_app.UserRegistrationPage
import com.example.atletica_ceavi_app.view.LoginPage
import com.example.atletica_ceavi_app.viewModel.AuthViewModel

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(navController,authViewModel)
        }
        composable("profile") {
           ProfilePage(navController,authViewModel)
        }
        composable("notifications") {
            NotificationsPage(navController,authViewModel)
        }
        composable("users") {
            UserRegistrationPage(navController,authViewModel, onUserRegistered = {})
        }
    }
}