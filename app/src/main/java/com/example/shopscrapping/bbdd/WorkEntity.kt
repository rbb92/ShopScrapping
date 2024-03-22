package com.example.shopscrapping.bbdd

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "Work",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["IDProducto"],
        childColumns = ["IDProducto"],
//        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkEntity(
    @PrimaryKey(autoGenerate = true) val IDWork: String,
    val IDProducto: Int,
    val stockAlerta: Boolean,
    val precioAlerta: Float,
    val periodo: Long,
    val talla: String?,
    val fechaInicio: Date,
    val numeroBusquedas: Int
)