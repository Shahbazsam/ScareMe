package com.example.scareme.authenticationScreen.domain.use_case

import android.util.Patterns

class ValidateRepeatedPassword {

    fun execute(password :String , repeatedPassword : String) : ValidationResult {
        if(password != repeatedPassword ){
            return ValidationResult(
                successful = false,
                errorMessage = " The password don't match "
            )
        }

        return ValidationResult(
            successful = true,
        )
    }
}