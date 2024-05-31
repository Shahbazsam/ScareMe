package com.example.scareme.signInScreen.data

import com.example.scareme.signInScreen.data.model.Token
import com.example.scareme.signInScreen.data.model.UserData
import com.example.scareme.signInScreen.network.SignInApiService

interface SignInRepository {
    suspend fun getRegistered(userData: UserData) : Token
}

class NetworkSignInRepository(
    private val signInApiService: SignInApiService
) : SignInRepository {

    override suspend fun getRegistered(userData: UserData): Token {
        return signInApiService.register(userData)
    }
}