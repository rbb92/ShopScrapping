package com.rbb92.cazachollo.bbdd

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Work",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["UUID"],
        childColumns = ["UUID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkEntity(
    @PrimaryKey() val UUID: String,
    val stockAlerta: Boolean,
    val todosPrecios: Boolean,  //A tener en cuenta todos los precios.
    val precioAlerta: Float,
    val periodo: Long,   // Cada cuanto se realiza el scrapeo
    val fechaInicio: Long,
    val numeroBusquedas: Int,
    val numeroBusquedasFallidas: Int,
    val numeroNotificaciones: Int,
    val fechaUltimaNotificacion: Long,
    val fechaUltimaBusqueda: Long
)