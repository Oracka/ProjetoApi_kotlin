package com.yuri.personagens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuri.personagens.data.Favorito
import com.yuri.personagens.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class FavoritosUiState( // armazenar nome
    val nomeUsuario: String = ""
)

class FavoritosViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()
    val favoritos: StateFlow<List<Favorito>> = repository.listarFavoritos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        carregarNomeUsuario()
    }
    private fun carregarNomeUsuario() {
        viewModelScope.launch {
            val nome = repository.buscarNomeUsuario() ?: "Usuario"
            // ✅ 3. Agora o _uiState existe e pode ser atualizado
            _uiState.update { it.copy(nomeUsuario = nome) }
        }
    }
    fun remover(characterId: Int) {
        viewModelScope.launch { repository.removerFavorito(characterId) }
    }
}