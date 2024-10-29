package com.example.atletica_ceavi_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atletica_ceavi_app.model.UserData
import com.example.atletica_ceavi_app.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    private val _loggedUser = MutableStateFlow<UserData?>(null)
    val users: StateFlow<List<UserData>> = _users
    val loggedUser: StateFlow<UserData?> = _loggedUser

    init {
        getLoggedUserData()
        getAllUsers()
    }

    private fun getAllUsers() {
        viewModelScope.launch {
            userRepository.getAllUsers { userList ->
                _users.value = userList
            }
        }
    }

    fun refreshUsers() {
        getAllUsers()
    }

    private fun getLoggedUserData() {
        userRepository.getLoggedUserData { userData ->
            _loggedUser.value = userData
        }
    }
}