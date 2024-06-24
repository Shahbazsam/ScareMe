package com.example.scareme.profile.network

import com.example.scareme.authenticationScreen.data.model.Token
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformation
import com.example.scareme.profile.data.model.UserInformationToSend
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileApiService {
    @Multipart
    @POST("profile/avatar")
    suspend fun updateAvatar(
        @Header("Authorization") token : String,
        @Part avatar : MultipartBody.Part
    )

    @GET("profile")
    suspend fun retrieveUserProfile(
        @Header("Authorization") token: String
    ): UserInformation

    @PATCH("profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body userInformation: UserInformationToSend
    )


    @GET("topic")
    suspend fun getTopics(
        @Header("Authorization")  token : String
    ): List<Topics>


}