package com.example.scareme.chat.data

import com.example.scareme.chat.network.ChatApiService
import com.example.scareme.profile.network.ProfileApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface ChatContainer{
        val chatRepository : ChatRepository
}

class DefaultChatContainer : ChatContainer{


    private val baseUrl = "http://itindr.mcenter.pro:8092/api/mobile/v1/"

    private val json = Json{
        ignoreUnknownKeys = true
    }

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client : OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory( json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService : ChatApiService by lazy {
        retrofit.create(ChatApiService::class.java)
    }

    override val chatRepository: ChatRepository by lazy {
        NetworkChatRepository(retrofitService)
    }
}