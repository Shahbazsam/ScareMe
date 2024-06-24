package com.example.scareme.signInScreen.data


import com.example.scareme.signInScreen.network.SignInApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


interface SignInAppContainer {

    val signInRepository : SignInRepository
}

class DefaultSignInContainer : SignInAppContainer {

    private val baseUrl = "http://itindr.mcenter.pro:8092/api/mobile/v1/"

    private val json = Json{
        ignoreUnknownKeys = true
    }

    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client: OkHttpClient = Builder().addInterceptor(interceptor).build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val retrofitservice : SignInApiService = retrofit.create(SignInApiService::class.java)

    override val signInRepository: SignInRepository =
        NetworkSignInRepository(retrofitservice)




}