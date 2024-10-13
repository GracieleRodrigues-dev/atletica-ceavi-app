package com.example.atletica_ceavi_app.view
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.viewModel.AuthState
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    val database = Firebase.database
    val myRef = database.getReference("message")

    myRef.setValue("Hello, World!")

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { Text(
        text = "Home Page",
        fontSize = 32.sp
    )
        TextButton(onClick = {
            authViewModel.signout()
        }) {
            Text(text = "Sair")
        }
    }
}