package com.example.atletica_ceavi_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.ui.components.navigation.DrawerLayout
import com.example.atletica_ceavi_app.viewModel.AuthViewModel

@Composable
fun NotificationsPage(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    DrawerLayout(navController,authViewModel)
}