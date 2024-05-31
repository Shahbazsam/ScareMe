package com.example.scareme.ui.authenticationScreen.data

import com.example.scareme.ui.authenticationScreen.data.model.Token
import com.example.scareme.ui.authenticationScreen.data.model.UserData
import com.example.scareme.ui.authenticationScreen.network.AuthApiService

interface AuthRegisterRepository {
    suspend fun getRegistered(userData: UserData) : Token
}

class NetworkAuthRegisterRepository(
    private val authApiService: AuthApiService
) : AuthRegisterRepository {

    override suspend fun getRegistered(userData: UserData): Token {
        return authApiService.register(userData)
    }
}