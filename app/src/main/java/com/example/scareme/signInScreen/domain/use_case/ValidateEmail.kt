package com.example.scareme.signInScreen.domain.use_case

import android.util.Patterns

class ValidateEmail {

    fun execute(email :String) : ValidationResult {
        if(email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = " The email can't be blank"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "Not a valid email"
            )
        }
        return ValidationResult(
            successful = true,
        )
    }
}