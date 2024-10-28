package com.example.atletica_ceavi_app.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atletica_ceavi_app.view.HomePage
import com.example.atletica_ceavi_app.view.LoginPage
import com.example.atletica_ceavi_app.view.NewSportPage
import com.example.atletica_ceavi_app.view.NewTeamPage
import com.example.atletica_ceavi_app.view.NotificationsPage
import com.example.atletica_ceavi_app.view.ProfilePage
import com.example.atletica_ceavi_app.view.SportListPage
import com.example.atletica_ceavi_app.view.TeamListPage
import com.example.atletica_ceavi_app.view.UserRegistrationPage
import com.example.atletica_ceavi_app.view.UsersListPage
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.UserViewModel

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, userViewModel: UserViewModel ) {
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
            UsersListPage(navController,authViewModel,userViewModel)
        }
        composable("userRegistration") {
            UserRegistrationPage(navController,authViewModel)
        }
        composable("sports") {
            SportListPage(navController,authViewModel)
        }
        composable("newSport") {
            NewSportPage(navController,authViewModel)
        }
        composable("teams") {
            TeamListPage(navController,authViewModel)
        }
        composable("newTeam") {
            NewTeamPage(navController,authViewModel)
        }
    }
}