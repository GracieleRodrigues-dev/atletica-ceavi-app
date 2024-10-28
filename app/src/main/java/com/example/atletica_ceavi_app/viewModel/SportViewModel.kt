package com.example.atletica_ceavi_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atletica_ceavi_app.model.Sport
import com.example.atletica_ceavi_app.model.SportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SportViewModel : ViewModel() {
    private val repository = SportRepository()
    private val _sports = MutableStateFlow<List<Sport>>(emptyList())
    val sports: StateFlow<List<Sport>> = _sports

    fun addSport(name: String, description: String) {
        val newSport = Sport(name = name, description = description)
        repository.addSport(newSport) { success ->
            if (success) getAllSports()
        }
    }

    fun updateSport(sport: Sport) {
        repository.updateSport(sport) { success ->
            if (success) getAllSports()
        }
    }

    fun deleteSport(sportId: String) {
        repository.deleteSport(sportId) { success ->
            if (success) getAllSports()
        }
    }

    fun getAllSports() {
        viewModelScope.launch {
            repository.getAllSports { sportList ->
                _sports.value = sportList
            }
        }
    }


    fun refreshSports() {
        getAllSports()
    }
}
