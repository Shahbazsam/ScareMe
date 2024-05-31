package com.example.scareme.ui.authenticationScreen.data.model

import com.example.scareme.ui.authenticationScreen.domain.use_case.ValidateRepeatedPassword

data class UserData(
    val email : String,
    val password : String,

)
