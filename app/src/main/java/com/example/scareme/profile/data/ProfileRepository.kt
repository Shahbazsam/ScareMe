package com.example.scareme.profile.data

import android.net.Uri
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformation
import com.example.scareme.profile.data.model.UserInformationToSend
import com.example.scareme.profile.network.ProfileApiService
import okhttp3.MultipartBody

interface ProfileRepository {
    suspend fun getUserProfile(token: String):UserInformation
    suspend fun updateAvatar(token : String , uri: MultipartBody.Part)
    suspend fun updateUserProfile(token : String , userInformation: UserInformationToSend)
    suspend fun getTopics( token: String ) : List<Topics>
}

class NetworkProfileRepository(
    private val profileApiService: ProfileApiService
) : ProfileRepository{

    override suspend fun getUserProfile(token: String) : UserInformation {
        return profileApiService.retrieveUserProfile(token)
    }

    override suspend fun updateAvatar(token: String, uri: MultipartBody.Part) {
        profileApiService.updateAvatar(token , uri )
    }
    override suspend fun getTopics(token: String): List<Topics> {
       return profileApiService.getTopics(token)
    }

    override suspend fun updateUserProfile(token: String, userInformation: UserInformationToSend) {
        profileApiService.updateUserProfile(token, userInformation)
    }
}