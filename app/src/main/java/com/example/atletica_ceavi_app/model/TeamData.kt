package com.example.atletica_ceavi_app.model

data class Team(
    val id: String?,
    val name: String?,
    val sport: Sport?,
    val coach: UserData?,
    val members: List<UserData>? = listOf()
){
    constructor() : this(null, null, null, null, null)
}