package com.example.scareme.ui.authenticationScreen.network

import com.example.scareme.ui.authenticationScreen.data.model.Token
import com.example.scareme.ui.authenticationScreen.data.model.UserData
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body userDate : UserData) : Token
}