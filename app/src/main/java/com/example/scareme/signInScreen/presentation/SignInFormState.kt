package com.example.scareme.signInScreen.presentation

data class SignInFormState(

    val email : String = "",
    val emailError : String ? = null,
    val password :String = "",
    val passwordError: String? = null,

)
