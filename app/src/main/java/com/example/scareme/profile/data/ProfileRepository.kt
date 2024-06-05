package com.example.scareme.profile.data

import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformation
import com.example.scareme.profile.network.ProfileApiService

interface ProfileRepository {
    
    suspend fun updateUserProfile(token : String , userInformation: UserInformation)
    suspend fun getTopics( token: String ) : List<Topics>
}

class NetworkProfileRepository(
    private val profileApiService: ProfileApiService
) : ProfileRepository{

    override suspend fun getTopics(token: String): List<Topics> {
       return profileApiService.getTopics(token)
    }

    override suspend fun updateUserProfile(token: String, userInformation: UserInformation) {
        profileApiService.updateUserProfile(token, userInformation)
    }
}