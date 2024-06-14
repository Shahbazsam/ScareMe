package com.example.scareme.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInformation(
    val userId : String,
    val name : String,
    val avatar : String?,
    val aboutMyself : String?,
    val topics : List<Topics>?
)

data class UserInformationToSend(
    val name : String,
    val aboutMyself : String?,
    val topics : List<String>?
)
