package com.example.atletica_ceavi_app.model

data class Training(
    val id: String? = "",
    val date: String?,
    val time: String?,
    val locationName: String?,
    val latitude: Double?,
    val longitude: Double?,
    val sport: Sport?,
    val teams: List<Team>?,
    val notes: String? = null,
    val isRecurring: Boolean? = false,
    val recurringDayOfWeek: Int? = null // Dia da semana (1 = domingo, 2 = segunda, etc.)
){
    constructor(): this(null, null, null, null, null, null, null, null, null, null, null)
}

