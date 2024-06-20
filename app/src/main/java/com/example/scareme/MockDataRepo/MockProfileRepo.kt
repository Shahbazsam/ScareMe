package com.example.scareme.MockDataRepo

import com.example.scareme.profile.data.ProfileRepository
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformation
import com.example.scareme.profile.data.model.UserInformationToSend
import com.example.scareme.profile.network.ProfileApiService



class MockProfileRepository(
) : ProfileRepository {
    override suspend fun getTopics(token: String): List<Topics> {
        // Return a list of fake topics
        return listOf(
            Topics(id = "1", title = "Topic 1"),
            Topics(id = "2", title = "Topic 2"),
            // ... add more fake topics
        )
    }
    override suspend fun getUserProfile(token: String) : List<UserInformation> {
        return listOf(
            UserInformation(
                userId = "123",
                name = "sam",
                avatar = null,
                aboutMyself = "something",
                topics = listOf(
                    Topics(id = "1", title = "Topic 1"),
                    Topics(id = "2", title = "Topic 2"),
                    // ... add more fake topics
                )
            )
        )
    }

    override suspend fun updateUserProfile(token: String, userInformation: UserInformationToSend) {

    }
    // ... mock implementations for other repository functions
}