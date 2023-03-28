package com.example.bandage.models.response_model

data class Product(
    val availableUnits: Int,
    val category: String,
    val createdAt: Long,
    val id: String,
    val images: List<String>,
    val meta: MetaX,
    val name: String,
    val price: String,
    val updatedAt: Any
)