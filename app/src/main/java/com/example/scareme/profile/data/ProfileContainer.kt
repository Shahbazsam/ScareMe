package com.example.scareme.profile.data

import com.example.scareme.profile.network.ProfileApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


interface ProfileContainer {
    val profileRepository : ProfileRepository
}

class DefaultProfileContainer : ProfileContainer{

    private val baseUrl = "http://itindr.mcenter.pro:8092/api/mobile/v1/"

    private val json = Json{
        ignoreUnknownKeys = true
    }

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client : OkHttpClient = Builder().addInterceptor(interceptor).build()

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory( json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService : ProfileApiService by lazy {
        retrofit.create(ProfileApiService::class.java)
    }


    override val profileRepository: ProfileRepository by lazy {
        NetworkProfileRepository(retrofitService)
    }

}