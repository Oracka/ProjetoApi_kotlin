package com.yuri.personagens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuri.personagens.data.Repository
import com.yuri.personagens.data.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginUiState(
    val isLoading: Boolean = false,
    val usuarioLogado: Usuario? = null,
    val erro: String? = null
)

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun entrar(email: String, senha: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            try {
                val usuario = repository.login(email, senha)
                if (usuario != null) {
                    _uiState.update { it.copy(isLoading = false, usuarioLogado = usuario) }
                } else {
                    _uiState.update { it.copy(isLoading = false, erro = "Email ou senha inválidos") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, erro = "Erro: ${e.message}") }
            }
        }
    }
}