package com.example.atletica_ceavi_app.ui.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atletica_ceavi_app.viewModel.AuthState
import com.example.atletica_ceavi_app.viewModel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun DrawerLayout(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier ,
    content: @Composable () -> Unit
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentDestination?.route ?: "Home"
    val screenName = menuItems.flatMap { item ->
        listOf(item) + (item.subItems ?: emptyList())
    }.find { it.destination == currentRoute }?.title ?: "Screen Name"

    val authState = authViewModel.authState.collectAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navController, authViewModel)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }, screenName = screenName, navController = navController
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    Text(
        text = "Atlética CEAVI",
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp)
    )

    HorizontalDivider()
    Spacer(modifier = Modifier.height(4.dp))

    menuItems.forEach { item ->
        var expanded by remember { mutableStateOf(false) }

        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title
                )
            },
            label = {
                Text(
                    text = item.title,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = {
                if (item.subItems != null) {
                    expanded = !expanded
                } else {
                    navController.navigate(item.destination)
                }
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (expanded && item.subItems != null) {
            item.subItems.forEach { subItem ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = subItem.icon,
                            contentDescription = subItem.title,
                            modifier = Modifier.padding(start = 15.dp)
                        )
                    },
                    label = {
                        Text(
                            text = subItem.title,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(subItem.destination)
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Sair"
            )
        },
        label = {
            Text(
                text = "Sair",
                fontSize = 17.sp,
                modifier = Modifier.padding(16.dp)
            )
        },
        selected = false,
        onClick = {
            authViewModel.signout()
        }
    )
}


@Composable
fun ScreenContent(modifier: Modifier = Modifier){
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onOpenDrawer: () -> Unit, screenName: String, navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(28.dp)
                    .clickable {
                        onOpenDrawer()
                    }
            )
        },
        title = {
            Text(text = screenName)
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate("notifications")
                    }
            )
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(28.dp)
                    .clickable {
                        navController.navigate("profile")
                    }
            )
        }
    )
}
