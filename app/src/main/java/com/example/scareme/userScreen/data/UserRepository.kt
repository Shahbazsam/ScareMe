package com.example.scareme.userScreen.data

import com.example.scareme.userScreen.data.model.UserData
import com.example.scareme.userScreen.network.UserApiService

interface UserRepository{

    suspend fun getUser(token : String ) : List<UserData>
    suspend fun likeUser(token : String , userId : String )
    suspend fun dislikeUser(token : String , userId : String )


}


class   NetworkUserRepository(
    private val userApiService: UserApiService
) : UserRepository{

    override suspend fun getUser(token: String): List<UserData> {
        return userApiService.getUsers(token)
    }

    override suspend fun dislikeUser(token: String, userId: String) {
        userApiService.disLikeUser(token , userId)
    }

    override suspend fun likeUser(token: String, userId: String) {
        userApiService.likeUser(token , userId)
    }

}