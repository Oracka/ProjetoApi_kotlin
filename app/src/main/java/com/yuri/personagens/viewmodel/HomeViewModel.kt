package com.yuri.personagens.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuri.personagens.data.PersonagemDto
import com.yuri.personagens.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val personagem: PersonagemDto? = null,
    val erro: String? = null,
    val favoritado: Boolean = false
)

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun pesquisar(termo: String) {
        if (termo.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null, personagem = null) }
            try {
                val resultado = repository.buscarPersonagem(termo)
                if (resultado != null) {
                    val jaFavoritado = repository.isFavorito(resultado.id)
                    _uiState.update { it.copy(isLoading = false, personagem = resultado,
                        favoritado = jaFavoritado) }
                } else {
                    _uiState.update { it.copy(isLoading = false, erro = "Personagem não encontrado") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, erro = "Erro na busca: ${e.message}") }
            }
        }
    }
    fun toggleFavorito() {
        val personagem = _uiState.value.personagem ?: return
        viewModelScope.launch {
            try {
                if (_uiState.value.favoritado) {
                    // Se já está favoritado, remove
                    repository.removerFavorito(personagem.id)
                    _uiState.update { it.copy(favoritado = false) }
                } else {
                    // Se não está, adiciona
                    repository.favoritar(personagem)
                    _uiState.update { it.copy(favoritado = true) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao alterar favorito: ${e.message}") }
            }
        }

    }
}