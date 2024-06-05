package com.example.scareme.profile.data

import com.example.scareme.profile.network.ProfileApiService
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ProfileContainer {
    val profileRepository : ProfileRepository
}

class DefaultProfileContainer : ProfileContainer{

    private val baseUrl = "http://itindr.mcenter.pro:8092/api/mobile/v1/"
    private val interceptor = HttpLoggingInterceptor()
    private val client : OkHttpClient = Builder().addInterceptor(interceptor).build()

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService : ProfileApiService by lazy {
        retrofit.create(ProfileApiService::class.java)
    }


    override val profileRepository: ProfileRepository by lazy {
        NetworkProfileRepository(retrofitService)
    }

}