package com.yuri.personagens.data

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("sessao", Context.MODE_PRIVATE)

    fun salvarSessao(usuarioId: Long) {
        prefs.edit().putLong("usuarioId", usuarioId).apply()
    }

    fun obterUsuarioId(): Long? {
        val id = prefs.getLong("usuarioId", -1L)
        return if (id == -1L) null else id
    }

    fun estaLogado(): Boolean = obterUsuarioId() != null

    fun encerrarSessao() {
        prefs.edit().remove("usuarioId").apply()
    }
}



















