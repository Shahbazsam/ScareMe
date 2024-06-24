package com.example.scareme.chat.network

import com.example.scareme.chat.data.model.ChatData
import com.example.scareme.chat.data.model.Messages
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {

    @GET("chat")
    suspend fun getChats(
        @Header("Authorization")  token : String
    ): List<ChatData>

    @GET("chat/{chatId}/message")
    suspend fun getMessages(
        @Header("Authorization") token : String,
        @Path("chatId") chatId :String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ):List<Messages>
}