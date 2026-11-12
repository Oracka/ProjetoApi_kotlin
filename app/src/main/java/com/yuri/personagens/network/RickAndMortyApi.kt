package com.yuri.personagens.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

import com.yuri.personagens.data.PersonagemDto
import com.yuri.personagens.data.PersonagemResponseDto
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character/{id}")
    suspend fun obterPersonaPorId(@Path("id") id: Int): Response<PersonagemDto>

    @GET("character")
    suspend fun obterPersonagemPorNome(@Query("name") name: String): Response<PersonagemResponseDto>

} // muito util pra criar diversas rotas em apenas um textfield ou consulta.