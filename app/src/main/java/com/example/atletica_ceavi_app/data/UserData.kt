package com.example.atletica_ceavi_app.data

data class UserData (
    val id: String?,
    val name: String?,
    val role: String?,
    val registrationDate: String?,
    val age: Int?,
    val gender: String?
) {
    constructor() : this(null, null, null, null, null, null)
}
