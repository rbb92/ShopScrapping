package com.example.shopscrapping.bbdd

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Work",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["UUID"],
        childColumns = ["UUID"],
//        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkEntity(
    @PrimaryKey() val UUID: String,
    val stockAlerta: Boolean,
    val precioAlerta: Float,
    val periodo: Long,
    val talla: String?,
    val fechaInicio: Long,
    val numeroBusquedas: Int
)