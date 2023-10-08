package com.noteapplication.models

data class UserResponse(
    val token: String,
    val user: User
)