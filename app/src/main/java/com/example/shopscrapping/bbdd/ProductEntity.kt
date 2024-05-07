package com.example.shopscrapping.bbdd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product")
data class ProductEntity(
    @PrimaryKey() val UUID: String ,
    val URL: String,
    val nombre: String,
    val descripcion: String,
    val precioInicial: Float,  //precio Inicial
    val precioActualGobal: Float,
    val precioActual: Float,
    val tienda: String,     //No se usa ya que se pasa como input data al Work
    val moneda: String,
    val region: String,     //usado por algunas store para actualizar cookies. //No se usa ya que se pasa como input data al Work
    val productId: String,  //diferenciar version de producto (talla, capacidad) para un producto, que usara
    val urlImagen: String,
    val urlReferido: String
)


