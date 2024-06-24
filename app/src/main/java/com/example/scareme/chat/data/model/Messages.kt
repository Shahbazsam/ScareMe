package com.example.scareme.chat.data.model

import kotlinx.serialization.Serializable


@Serializable
data class Messages(
    val id :String?,
    val text : String?,
    val user : User
)