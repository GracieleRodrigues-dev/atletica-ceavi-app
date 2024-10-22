package com.example.atletica_ceavi_app.model

import com.example.atletica_ceavi_app.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun registerUserInDatabase(name: String, role: String, age: Int, gender: String) {
        val user = auth.currentUser
        if (user != null) {
            val userData = UserData(
                id = user.uid,
                name = name,
                role = role,
                registrationDate = System.currentTimeMillis().toString(),
                age = age,
                gender = gender
            )

            database.child(user.uid).setValue(userData)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
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
                .addOnFailureListener {
                    onUserDataReceived(null)
                }
        } else {
            onUserDataReceived(null)
        }
    }

    fun getAllUsers(onUsersReceived: (List<UserData>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = mutableListOf<UserData>()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserData::class.java)
                    user?.let { users.add(it) }
                }
                onUsersReceived(users)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onUsersReceived(emptyList())
            }
        })
    }
}
