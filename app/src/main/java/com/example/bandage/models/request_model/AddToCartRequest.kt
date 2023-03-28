package com.example.bandage.models.request_model

data class AddToCartRequest(
    val amount: String,
    val productId: String
)