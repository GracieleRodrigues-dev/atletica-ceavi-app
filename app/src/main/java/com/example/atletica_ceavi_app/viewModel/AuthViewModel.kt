package com.example.atletica_ceavi_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atletica_ceavi_app.model.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository: UserRepository = UserRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email:String,password:String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("E-mail ou senha não podem estar vazios")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Erro ao realizar login")
                }
            }
    }

    fun signup(
        email: String,
        password: String,
        name: String,
        birthDate: String,
        gender: String,
        role: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || birthDate.isEmpty()) {
            onResult(false, "Todos os campos são obrigatórios")
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            userRepository.registerUserInDatabase(
                                userId = user.uid,
                                name = name,
                                role = role,
                                birthDate = birthDate,
                                gender = gender
                            )
                            onResult(true, null)
                        }
                    } else {
                        onResult(false, task.exception?.message ?: "Erro ao criar usuário")
                    }
                }
        }
    }


    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}


sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message:String) : AuthState()
}