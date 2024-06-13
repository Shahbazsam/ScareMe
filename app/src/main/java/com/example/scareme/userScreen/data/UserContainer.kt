package com.example.scareme.userScreen.data

import com.example.scareme.profile.network.ProfileApiService
import com.example.scareme.userScreen.network.UserApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface UserContainer{
    val userRepository : UserRepository
}

class DefaultUserContainer : UserContainer{

    private val baseUrl = "http://itindr.mcenter.pro:8092/api/mobile/v1/"
    private val interceptor = HttpLoggingInterceptor()
    private val client : OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory( Json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService : UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(retrofitService)
    }
}