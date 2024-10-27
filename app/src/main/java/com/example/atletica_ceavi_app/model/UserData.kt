package com.example.atletica_ceavi_app.model

data class UserData(
    val id: String?,
    val name: String?,
    val role: String?,
    val registrationDate: String?,
    val birthDate: String?,
    val gender: String?
) {
    constructor() : this(null, null, null, null, null, null)
}