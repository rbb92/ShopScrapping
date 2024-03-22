package com.example.shopscrapping.bbdd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val IDProducto: Int = 0,
    val URL: String,
    val nombre: String,
    val descripcion: String,
    val precio: Float,
    val tienda: String,
    val urlImagen: String,
    val urlReferido: String
)


