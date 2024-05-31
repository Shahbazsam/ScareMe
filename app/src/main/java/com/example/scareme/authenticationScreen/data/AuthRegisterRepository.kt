package com.example.scareme.authenticationScreen.data

import com.example.scareme.authenticationScreen.data.model.Token
import com.example.scareme.authenticationScreen.data.model.UserData
import com.example.scareme.authenticationScreen.network.AuthApiService

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