package com.example.scareme.signInScreen.domain.use_case


data class ValidationResult(
    val successful : Boolean,
    val errorMessage: String ? = null
)
