package com.example.scareme.authenticationScreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val accessToken : String
)
