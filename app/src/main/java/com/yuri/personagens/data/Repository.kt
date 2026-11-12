package com.yuri.personagens.data

import android.util.Log
import com.yuri.personagens.network.RickAndMortyApi
import kotlinx.coroutines.flow.Flow

class Repository(
    private val usuarioDao: UsuarioDao,
    private val favoritoDao: FavoritoDao,
    private val api: RickAndMortyApi,
    private val sessionManager: SessionManager
) {

    suspend fun cadastrar(nome: String, email: String, senha: String): Long {
        val usuario = Usuario(name = nome, email = email, senha = senha)
        return usuarioDao.inserir(usuario)
    }

    suspend fun login(email: String, senha: String): Usuario? {
        val usuario = usuarioDao.autenticar(email, senha)
        if (usuario != null) sessionManager.salvarSessao(usuario.id)
        return usuario
    }

    fun estaLogado(): Boolean = sessionManager.estaLogado()
    fun usuarioAtualId(): Long? = sessionManager.obterUsuarioId()
    fun sair() = sessionManager.encerrarSessao()

    suspend fun buscarNomeUsuario(): String? {
        val usuarioId = sessionManager.obterUsuarioId() ?: return  null
        return  usuarioDao.buscarPorId(usuarioId)?.name
    }

    suspend fun buscarPersonagem(termo: String): PersonagemDto? {
        if (termo.isBlank()) return null

        return try {
            val id = termo.toIntOrNull()
            if (id != null) {
                // CORRIGIDO: Adicionado o "gem" em obterPersonagemPorId
                val response = api.obterPersonaPorId(id)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } else {
                val response = api.obterPersonagemPorNome(termo)
                if (response.isSuccessful) {
                    // CORRIGIDO: Mudado de 'resultados' para 'results'
                    response.body()?.resultados?.firstOrNull()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ERRO_API", "Falha na busca: ${e.message}", e)
            null
        }
    }

    suspend fun favoritar(personagem: PersonagemDto) {
        val usuarioId = sessionManager.obterUsuarioId() ?: return
        favoritoDao.inserir(
            Favorito(
                usuarioId = usuarioId,
                characterId = personagem.id,
                nome = personagem.name,
                imagem = personagem.image
            )
        )
    }

    suspend fun isFavorito(characterId: Int): Boolean {
        val usuarioId = sessionManager.obterUsuarioId() ?: return false
        return favoritoDao.isFavorito(usuarioId, characterId)
    }

    suspend fun removerFavorito(characterId: Int) {
        val usuarioId = sessionManager.obterUsuarioId() ?: return
        favoritoDao.remover(usuarioId, characterId)
    }

    fun listarFavoritos(): Flow<List<Favorito>> {
        val usuarioId = sessionManager.obterUsuarioId() ?: -1L
        return favoritoDao.listarPorUsuario(usuarioId)
    }
}