package com.example.bandage.models.request_model

data class UpdateQuantityRequest(
    val productId: String,
    val quantity: Int
)