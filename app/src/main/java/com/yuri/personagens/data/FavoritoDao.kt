package com.yuri.personagens.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(favorito: Favorito)

    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId ORDER BY nome ASC")
    fun listarPorUsuario(usuarioId: Long): Flow<List<Favorito>>

    @Query("DELETE FROM favoritos WHERE usuarioId = :usuarioId AND characterId = :characterId")
    suspend fun remover(usuarioId: Long, characterId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE usuarioId = :usuarioId AND characterId = :characterId)")
    suspend fun isFavorito(usuarioId: Long, characterId: Int): Boolean


}

