package com.example.atletica_ceavi_app.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeamRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("teams")

    fun addTeam(team: Team, onComplete: (Boolean) -> Unit) {
        val teamId = database.push().key ?: return
        val newTeam = team.copy(id = teamId)
        database.child(teamId).setValue(newTeam)
            .addOnSuccessListener {
                Log.d("TeamRepository", "Equipe cadastrada com sucesso: $newTeam")
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e("TeamRepository", "Erro ao cadastrar equipe: ${it.message}")
                onComplete(false)
            }
    }

    fun updateTeam(team: Team, onComplete: (Boolean) -> Unit) {
        team.id?.let { teamId ->
            database.child(teamId).setValue(team)
                .addOnSuccessListener {
                    Log.d("TeamRepository", "Equipe atualizada com sucesso: $team")
                    onComplete(true)
                }
                .addOnFailureListener {
                    Log.e("TeamRepository", "Erro ao atualizar equipe: ${it.message}")
                    onComplete(false)
                }
        }
    }

    fun deleteTeam(teamId: String, onComplete: (Boolean) -> Unit) {
        database.child(teamId).removeValue()
            .addOnSuccessListener {
                Log.d("TeamRepository", "Equipe removida com sucesso: $teamId")
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e("TeamRepository", "Erro ao remover equipe: ${it.message}")
                onComplete(false)
            }
    }

    fun getAllTeams(onTeamsReceived: (List<Team>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val teams = mutableListOf<Team>()
                for (snapshot in dataSnapshot.children) {
                    val team = snapshot.getValue(Team::class.java)
                    team?.let { teams.add(it) }
                }
                onTeamsReceived(teams)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onTeamsReceived(emptyList())
            }
        })
    }
}
