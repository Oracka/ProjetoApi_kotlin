package com.yuri.personagens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuri.personagens.data.Favorito
import com.yuri.personagens.data.Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repository: Repository) : ViewModel() {
    val favoritos: StateFlow<List<Favorito>> = repository.listarFavoritos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun remover(characterId: Int) {
        viewModelScope.launch { repository.removerFavorito(characterId) }
    }
}