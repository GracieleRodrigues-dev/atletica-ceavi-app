package com.example.atletica_ceavi_app.model

data class Sport(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null
){
    override fun toString(): String {
        return name ?: "Modalidade não disponível"
    }
}