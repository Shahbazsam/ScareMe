package com.example.scareme.signInScreen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val email : String,
    val password : String,

)
