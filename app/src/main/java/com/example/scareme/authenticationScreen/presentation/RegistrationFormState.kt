package com.example.scareme.authenticationScreen.presentation

data class RegistrationFormState(

    val email : String = "",
    val emailError : String ? = null,
    val password :String = "",
    val passwordError: String? = null,
    val repeatedPassword : String = "" ,
    val repeatedPasswordError: String ? = null
)
