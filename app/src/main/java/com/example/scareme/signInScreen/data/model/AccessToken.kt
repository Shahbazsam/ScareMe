package com.example.scareme.signInScreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
    val token : String = ""
)
