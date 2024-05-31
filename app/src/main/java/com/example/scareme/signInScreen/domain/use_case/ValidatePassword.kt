package com.example.scareme.signInScreen.domain.use_case

class ValidatePassword {

    fun execute(password :String) : ValidationResult {
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