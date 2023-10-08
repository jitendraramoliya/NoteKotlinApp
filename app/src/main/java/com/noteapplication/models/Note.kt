package com.noteapplication.models

data class Note(
    val userId: String,
    val title: String,
    val description: String,
    val createdAt: String,
)