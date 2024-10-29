package com.example.atletica_ceavi_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atletica_ceavi_app.model.Sport
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

    private val _sport = MutableStateFlow<Sport?>(null)
    val sport: StateFlow<Sport?> get() = _sport

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> get() = _teams

    private val _notes = MutableStateFlow<String>("")
    val notes: StateFlow<String> get() = _notes

    private val _isRecurring = MutableStateFlow(false)
    val isRecurring: StateFlow<Boolean> get() = _isRecurring

    private val _recurringDayOfWeek = MutableStateFlow<Int?>(null)
    val recurringDayOfWeek: StateFlow<Int?> get() = _recurringDayOfWeek

    private val _sports = MutableStateFlow<List<Sport>>(emptyList())
    val sports: StateFlow<List<Sport>> = _sports

    private val sportRepository = SportRepository()
    private val teamRepository = TeamRepository()


    fun updateDate(newDate: String) { _date.value = newDate }
    fun updateTime(newTime: String) { _time.value = newTime }
    fun updateLocationName(newLocationName: String) { _locationName.value = newLocationName }
    fun updateSelectedLocation(latitude: Double, longitude: Double) { _selectedLocation.value = Pair(latitude, longitude) }
    fun updateSport(newSport: Sport) { _sport.value = newSport }
    fun updateTeams(newTeams: List<Team>) { _teams.value = newTeams }
    fun updateNotes(newNotes: String) { _notes.value = newNotes }
    fun setIsRecurring(isRecurring: Boolean) { _isRecurring.value = isRecurring }
    fun updateRecurringDayOfWeek(dayOfWeek: Int?) { _recurringDayOfWeek.value = dayOfWeek }


    init {
        loadSports()
        loadTeams()
    }

    private fun loadSports() {
        viewModelScope.launch {
            sportRepository.getAllSports { sportsList ->
                _sports.value = sportsList
            }
        }
    }

    private fun loadTeams() {
        viewModelScope.launch {
            teamRepository.getAllTeams { teamList ->
                _teams.value = teamList
            }
        }
    }

    fun addTraining() {
        viewModelScope.launch {
            val newTraining = Training(
                date = _date.value,
                time = _time.value,
                locationName = _locationName.value,
                latitude = _selectedLocation.value?.first ?: 0.0,
                longitude = _selectedLocation.value?.second ?: 0.0,
                sport = _sport.value ?: throw IllegalArgumentException("Sport is required"),
                teams = _teams.value,
                notes = _notes.value.takeIf { it.isNotBlank() },
                isRecurring = _isRecurring.value,
                recurringDayOfWeek = _recurringDayOfWeek.value
            )
            trainingRepository.addTraining(newTraining)
        }
    }
}