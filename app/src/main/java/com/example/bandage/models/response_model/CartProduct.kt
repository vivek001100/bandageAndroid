package com.example.bandage.models.response_model

data class CartProduct(
    val amount: String,
    val availableUnits: Int,
    val category: String,
    val createdAt: Int,
    val id: String,
    val images: List<String>,
    val meta: Meta,
    val name: String,
    val price: String,
    val productId: String,
    var quantity: Int,
    val updatedAt: Any,
    val userId: String
)
lateinit var cartProductsList: ArrayList<CartProduct>