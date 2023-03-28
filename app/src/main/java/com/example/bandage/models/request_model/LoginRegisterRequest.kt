package com.example.bandage.models.request_model

data class LoginRegisterRequest(
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val password: String
)