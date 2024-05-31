package com.example.scareme.authenticationScreen.data

import com.example.scareme.authenticationScreen.network.AuthApiService
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit


interface AuthAppContainer {

    val authRegisterRepository : AuthRegisterRepository
}

class DefaultContainer : AuthAppContainer {

    private val baseUrl = "http://itindr.mcenter.pro:8092/api/mobile/v1/"

    val interceptor = HttpLoggingInterceptor()

    val client: OkHttpClient = Builder().addInterceptor(interceptor).build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitservice : AuthApiService = retrofit.create(AuthApiService::class.java)

    override val authRegisterRepository: AuthRegisterRepository =
        NetworkAuthRegisterRepository(retrofitservice)

}