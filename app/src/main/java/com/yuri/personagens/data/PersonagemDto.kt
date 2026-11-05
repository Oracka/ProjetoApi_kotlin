package com.yuri.personagens.data

data class PersonagemDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val location: LocalDto,
    val image: String
)

data class LocalDto(val name: String)