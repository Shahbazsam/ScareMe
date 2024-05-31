package com.example.scareme.ui.authenticationScreen.domain.use_case

import android.util.Patterns

class ValidatePassword {

    fun execute(password :String) : ValidationResult{
        if(password.length < 8 ){
            return ValidationResult(
                successful = false,
                errorMessage = " The password length is less than 8"
            )
        }
        val containsLettersAndDigits = password.any{ it.isDigit()} &&
                    password.any{ it.isLetter() }
        if (!containsLettersAndDigits){
            return ValidationResult(
                successful = false,
                errorMessage = "Password needs to contain at least one letter and digit"
            )
        }
        return ValidationResult(
            successful = true,
        )
    }
}