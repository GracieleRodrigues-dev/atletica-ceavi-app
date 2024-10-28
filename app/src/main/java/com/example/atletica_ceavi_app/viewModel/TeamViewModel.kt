package com.example.atletica_ceavi_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atletica_ceavi_app.model.Sport
import com.example.atletica_ceavi_app.model.SportRepository
import com.example.atletica_ceavi_app.model.Team
import com.example.atletica_ceavi_app.model.TeamRepository
import com.example.atletica_ceavi_app.model.UserData
import com.example.atletica_ceavi_app.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {
    private val repository = TeamRepository()

    private val userRepository = UserRepository()
    private val sportRepository = SportRepository()


    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    private val _sports = MutableStateFlow<List<Sport>>(emptyList())
    val sports: StateFlow<List<Sport>> = _sports

    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    val users: StateFlow<List<UserData>> = _users

    private val _selectedSport = MutableStateFlow<Sport?>(null)
    val selectedSport: StateFlow<Sport?> = _selectedSport

    private val _selectedCoach = MutableStateFlow<UserData?>(null)
    val selectedCoach: StateFlow<UserData?> = _selectedCoach

    private val _selectedAthletes = MutableStateFlow<List<UserData>>(emptyList())
    val selectedAthletes: StateFlow<List<UserData>> = _selectedAthletes

    init {
        loadSports()
        loadUsers()
    }

    private fun loadSports() {
        viewModelScope.launch {
            sportRepository.getAllSports { sportsList ->
                _sports.value = sportsList
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            userRepository.getAllUsers { userList ->
                _users.value = userList
            }
        }
    }

    fun selectSport(sport: Sport) {
        _selectedSport.value = sport
    }

    fun selectCoach(coach: UserData) {
        _selectedCoach.value = coach
    }

    fun toggleAthleteSelection(athlete: UserData) {
        _selectedAthletes.value = if (_selectedAthletes.value.contains(athlete)) {
            _selectedAthletes.value - athlete
        } else {
            _selectedAthletes.value + athlete
        }
    }

    fun createTeam(name: String, sport: Sport?, coach: UserData?, members: List<UserData>) {
        if (sport != null && coach != null) {
            val newTeam = Team(id = null, name = name, sport = sport, coach = coach, members = members)
            repository.addTeam(newTeam) { success ->
                if (success) getAllTeams()
            }
        }
    }

    private fun getAllTeams() {
        viewModelScope.launch {
            repository.getAllTeams { teamList ->
                _teams.value = teamList
            }
        }
    }

    fun refreshTeams() {
        getAllTeams()
    }
}
