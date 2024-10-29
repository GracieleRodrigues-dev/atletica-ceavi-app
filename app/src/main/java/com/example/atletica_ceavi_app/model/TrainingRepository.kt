package com.example.atletica_ceavi_app.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TrainingRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("trainings")

    fun addTraining(training: Training, onComplete: (Boolean) -> Unit) {
        val trainingId = database.push().key ?: return
        val newTraining = training.copy(id = trainingId)
        database.child(trainingId).setValue(newTraining)
            .addOnSuccessListener {
                Log.d("TrainingRepository", "Treino cadastrado com sucesso: $newTraining")
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e("TrainingRepository", "Erro ao cadastrar treino: ${it.message}")
                onComplete(false)
            }
    }

    fun updateTraining(training: Training, onComplete: (Boolean) -> Unit) {
        training.id?.let { trainingId ->
            database.child(trainingId).setValue(training)
                .addOnSuccessListener {
                    Log.d("TrainingRepository", "Treino atualizado com sucesso: $training")
                    onComplete(true)
                }
                .addOnFailureListener {
                    Log.e("TrainingRepository", "Erro ao atualizar treino: ${it.message}")
                    onComplete(false)
                }
        }
    }

    fun deleteTraining(trainingId: String, onComplete: (Boolean) -> Unit) {
        database.child(trainingId).removeValue()
            .addOnSuccessListener {
                Log.d("TrainingRepository", "Treino removido com sucesso: $trainingId")
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e("TrainingRepository", "Erro ao remover treino: ${it.message}")
                onComplete(false)
            }
    }

    fun getAllTrainings(onTrainingsReceived: (List<Training>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val trainings = mutableListOf<Training>()
                for (snapshot in dataSnapshot.children) {
                    val training = snapshot.getValue(Training::class.java)
                    training?.let { trainings.add(it) }
                }
                onTrainingsReceived(trainings)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onTrainingsReceived(emptyList())
            }
        })
    }
}
