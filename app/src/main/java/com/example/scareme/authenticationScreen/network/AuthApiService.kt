package com.example.scareme.authenticationScreen.network

import com.example.scareme.authenticationScreen.data.model.Token
import com.example.scareme.authenticationScreen.data.model.UserData
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body userDate : UserData) : Token
}