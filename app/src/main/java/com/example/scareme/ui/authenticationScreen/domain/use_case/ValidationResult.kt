package com.example.scareme.ui.authenticationScreen.domain.use_case


data class ValidationResult(
    val successful : Boolean,
    val errorMessage: String ? = null
)
