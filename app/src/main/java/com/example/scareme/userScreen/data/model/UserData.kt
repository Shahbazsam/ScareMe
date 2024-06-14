package com.example.scareme.userScreen.data.model

import com.example.scareme.profile.data.model.Topics
import kotlinx.serialization.Serializable


@Serializable
data class UserData(
    val userId : String ,
    val name : String,
    val aboutMyself : String?,
    val avatar: String?,
    val topics : List<Topics>?
)
