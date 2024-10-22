package com.example.atletica_ceavi_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atletica_ceavi_app.data.UserData
import com.example.atletica_ceavi_app.model.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    fun getAllUsers(onUsersReceived: (List<UserData>) -> Unit) {
        viewModelScope.launch {
            userRepository.getAllUsers(onUsersReceived)
        }
    }
}
