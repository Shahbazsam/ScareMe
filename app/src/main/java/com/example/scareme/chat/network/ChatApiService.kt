package com.example.scareme.chat.network

import retrofit2.http.GET
import retrofit2.http.Header

interface ChatApiService {

    @GET("chat")
    suspend fun getChats(
        @Header("Authorization")  token : String
    )
}