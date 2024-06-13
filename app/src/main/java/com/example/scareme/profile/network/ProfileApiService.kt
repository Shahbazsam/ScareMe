package com.example.scareme.profile.network

import com.example.scareme.authenticationScreen.data.model.Token
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformation
import com.example.scareme.profile.data.model.UserInformationToSend
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface ProfileApiService {

    @GET("profile")
    suspend fun retrieveUserProfile(
        @Header("Authorization") token: String
    ): List<UserInformation>

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