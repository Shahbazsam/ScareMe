package com.example.scareme.signInScreen.network

import com.example.scareme.signInScreen.data.model.Token
import com.example.scareme.signInScreen.data.model.UserData
import retrofit2.http.Body
import retrofit2.http.POST


interface SignInApiService {

    @POST("auth/register")
    suspend fun register(@Body userDate : UserData) : Token
}