package com.example.scareme.userScreen.network

import com.example.scareme.userScreen.data.model.UserData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("user/feed")
    suspend fun getUsers(
        @Header("Authorization") token : String,
    ) : List<UserData>

    @POST("user/{userId}/like")
    suspend fun likeUser(
        @Header("Authorization") token : String,
        @Path("userId") userId : String,
    )

    @POST("user/{userId}/dislike")
    suspend fun disLikeUser(
        @Header("Authorization") token : String,
        @Path("userId") userId : String,
    )
}