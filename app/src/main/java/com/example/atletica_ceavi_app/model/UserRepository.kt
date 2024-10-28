package com.example.atletica_ceavi_app.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun registerUserInDatabase(
        userId: String,
        name: String,
        role: String,
        birthDate: String,
        gender: String
    ) {
        val userData = UserData(
            id = userId,
            name = name,
            role = role,
            registrationDate = LocalDate.now().toString(),
            birthDate = birthDate,
            gender = gender
        )

        database.child(userId).setValue(userData)
            .addOnSuccessListener {
                Log.d("UserRepository", "Usuário registrado com sucesso: $userData")
            }
            .addOnFailureListener { exception ->
                Log.e("UserRepository", "Erro ao registrar usuário: ${exception.message}")
            }
    }

    fun getLoggedUserData(onUserDataReceived: (UserData?) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            database.child(user.uid).get()
                .addOnSuccessListener { dataSnapshot ->
                    val userData = dataSnapshot.getValue(UserData::class.java)
                    onUserDataReceived(userData)
                }
                .addOnFailureListener { exception ->
                    Log.e("UserRepository", "Erro ao buscar dados do usuário: ${exception.message}")
                    onUserDataReceived(null)
                }
        } else {
            onUserDataReceived(null)
        }
    }

    fun getAllUsers(onUsersReceived: (List<UserData>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = mutableListOf<UserData>()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserData::class.java)
                    user?.let { users.add(it) }
                }
                onUsersReceived(users)
                Log.d("UserRepository", "Usuários recebidos: $users")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("UserRepository", "Erro ao buscar usuários: ${databaseError.message}")
                onUsersReceived(emptyList())
            }
        })
    }
}
