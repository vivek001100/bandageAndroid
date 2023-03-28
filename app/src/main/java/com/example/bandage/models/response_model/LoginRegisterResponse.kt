package com.example.bandage.models.response_model

import com.example.bandage.models.User

data class LoginRegisterResponse(
    val status: String,
    val token: String,
    val user: User
)