package com.example.atletica_ceavi_app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atletica_ceavi_app.model.SportRepository
import com.example.atletica_ceavi_app.model.Team
import com.example.atletica_ceavi_app.model.TeamRepository
import com.example.atletica_ceavi_app.model.Training
import com.example.atletica_ceavi_app.model.TrainingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrainingViewModel(
    private val trainingRepository: TrainingRepository = TrainingRepository()
) : ViewModel() {

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> get() = _date

    private val _time = MutableStateFlow("")
    val time: StateFlow<String> get() = _time

    private val _locationName = MutableStateFlow("")
    val locationName: StateFlow<String> get() = _locationName

    private val _selectedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val selectedLocation: StateFlow<Pair<Double, Double>?> get() = _selectedLocation

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> get() = _teams

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> get() = _notes

    private val _isRecurring = MutableStateFlow(false)
    val isRecurring: StateFlow<Boolean> get() = _isRecurring

    private val _recurringDayOfWeek = MutableStateFlow<Int?>(null)
    val recurringDayOfWeek: StateFlow<Int?> get() = _recurringDayOfWeek


    private val _selectedTeam = MutableStateFlow<Team?>(null)
    val selectedTeam: StateFlow<Team?> get() = _selectedTeam

    private val _selectedTeams = MutableStateFlow<List<Team>>(emptyList())
    val selectedTeams: StateFlow<List<Team>> get() = _selectedTeams

    private val _trainings = MutableStateFlow<List<Training>>(emptyList())
    val training: StateFlow<List<Training>> = _trainings

    private val sportRepository = SportRepository()
    private val teamRepository = TeamRepository()

    fun updateDate(newDate: String) { _date.value = newDate }
    fun updateTime(newTime: String) { _time.value = newTime }
    fun updateLocationName(newLocationName: String) { _locationName.value = newLocationName }
    fun updateSelectedLocation(latitude: Double, longitude: Double) { _selectedLocation.value = Pair(latitude, longitude) }
    fun updateTeams(newTeams: List<Team>) { _teams.value = newTeams }
    fun updateNotes(newNotes: String) { _notes.value = newNotes }
    fun setIsRecurring(isRecurring: Boolean) { _isRecurring.value = isRecurring }
    fun updateRecurringDayOfWeek(dayOfWeek: Int?) { _recurringDayOfWeek.value = dayOfWeek }

    fun updateSelectedTeam(newTeam: Team) {
        _selectedTeam.value = newTeam
    }

    init {
        loadTeams()
        getAllTrainings()
    }

    private fun loadTeams() {
        viewModelScope.launch {
            teamRepository.getAllTeams { teamList ->
                _teams.value = teamList
                Log.d("TrainingViewModel", "Loaded teams: $teamList")
            }
        }
    }

    fun addSelectedTeam(team: Team) {
        _selectedTeams.value = _selectedTeams.value + team
    }
    fun removeSelectedTeam(team: Team) {
        _selectedTeams.value = _selectedTeams.value.filterNot { it == team }
    }

    private fun getAllTrainings() {
        viewModelScope.launch {
            trainingRepository.getAllTrainings { trainingList ->
                _trainings.value = trainingList
            }
        }
    }

    fun refreshTrainings(){
        getAllTrainings()
    }

    fun addTraining() {
        viewModelScope.launch {
            val newTraining = Training(
                date = _date.value,
                time = _time.value,
                locationName = _locationName.value,
                latitude = _selectedLocation.value?.first ?: 0.0,
                longitude = _selectedLocation.value?.second ?: 0.0,
                teams = _selectedTeams.value,
                notes = _notes.value.takeIf { it.isNotBlank() },
                isRecurring = _isRecurring.value,
                recurringDayOfWeek = _recurringDayOfWeek.value
            )
            trainingRepository.addTraining(newTraining){ success ->
                if (success) getAllTrainings()
            }
        }
    }
}
