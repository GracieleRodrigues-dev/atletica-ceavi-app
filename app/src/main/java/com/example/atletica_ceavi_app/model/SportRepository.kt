package com.example.atletica_ceavi_app.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SportRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("sports")

    fun addSport(sport: Sport, onComplete: (Boolean) -> Unit) {
        val sportId = database.push().key ?: return
        val newSport = sport.copy(id = sportId)
        database.child(sportId).setValue(newSport)
            .addOnSuccessListener {
                Log.d("SportRepository", "Modalidade cadastrada com sucesso: $newSport")
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e("SportRepository", "Erro ao cadastrar modalidade: ${it.message}")
                onComplete(false)
            }
    }

    fun updateSport(sport: Sport, onComplete: (Boolean) -> Unit) {
        sport.id?.let { sportId ->
            database.child(sportId).setValue(sport)
                .addOnSuccessListener {
                    Log.d("SportRepository", "Modalidade atualizada com sucesso: $sport")
                    onComplete(true)
                }
                .addOnFailureListener {
                    Log.e("SportRepository", "Erro ao atualizar modalidade: ${it.message}")
                    onComplete(false)
                }
        }
    }

    fun deleteSport(sportId: String, onComplete: (Boolean) -> Unit) {
        database.child(sportId).removeValue()
            .addOnSuccessListener {
                Log.d("SportRepository", "Modalidade removida com sucesso: $sportId")
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e("SportRepository", "Erro ao remover modalidade: ${it.message}")
                onComplete(false)
            }
    }

    fun getAllSports(onSportsReceived: (List<Sport>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sports = mutableListOf<Sport>()
                for (snapshot in dataSnapshot.children) {
                    val sport = snapshot.getValue(Sport::class.java)
                    sport?.let { sports.add(it) }
                }
                onSportsReceived(sports)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onSportsReceived(emptyList())
            }
        })
    }
}
