package com.yuri.personagens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuri.personagens.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CadastroUiState(
    val isLoading: Boolean = false,
    val cadastroConcluido: Boolean = false,
    val erro: String? = null
)

class CadastroViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    fun cadastrar(nome: String, email: String, senha: String) {
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            _uiState.update { it.copy(erro = "Preencha todos os campos") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            try {
                repository.cadastrar(nome, email, senha)
                _uiState.update { it.copy(isLoading = false, cadastroConcluido = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, erro = "Erro ao cadastrar: ${e.message}") }
            }
        }
    }
}