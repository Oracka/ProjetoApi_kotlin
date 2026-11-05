package com.yuri.personagens.data


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "favoritos",
    primaryKeys = ["usuarioId", "characterId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("usuarioId")]
)
data class Favorito(
    val usuarioId: Long,
    val characterId: Int,
    val nome: String,
    val imagem: String
)