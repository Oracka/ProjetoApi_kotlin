package com.yuri.personagens.data

import com.google.gson.annotations.SerializedName

data class PersonagemResponseDto(
    @SerializedName("results") // !
    val resultados: List<PersonagemDto>
)