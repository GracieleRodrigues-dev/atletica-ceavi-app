package com.example.atletica_ceavi_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.atletica_ceavi_app.ui.components.navigation.MyAppNavigation
import com.example.atletica_ceavi_app.ui.theme.AtleticaceaviappTheme
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.example.atletica_ceavi_app.viewModel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val AuthViewModel: AuthViewModel by viewModels()
        val UserViewModel: UserViewModel by viewModels()
        setContent {
            AtleticaceaviappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = AuthViewModel,
                        userViewModel = UserViewModel
                    )
                }
            }
        }
    }
}

