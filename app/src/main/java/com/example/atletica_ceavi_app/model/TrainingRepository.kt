package com.example.atletica_ceavi_app.model

import kotlinx.coroutines.flow.Flow

class TrainingRepository {

    private val trainings = mutableListOf<Training>()

    fun addTraining(training: Training) {
        trainings.add(training)
    }

    fun updateTraining(training: Training) {
        val index = trainings.indexOfFirst { it.id == training.id }
        if (index != -1) {
            trainings[index] = training
        }
    }

    fun deleteTraining(trainingId: String) {
        trainings.removeAll { it.id == trainingId }
    }


    fun getTrainings(): Flow<List<Training>> {
        return kotlinx.coroutines.flow.flowOf(trainings)
    }


    fun confirmAttendance(trainingId: String, userId: String) {

    }


    fun getAttendanceList(trainingId: String): List<String> {
        return listOf()
    }
}
