package com.example.scareme.chat.data.model


import kotlinx.serialization.Serializable

@Serializable
data class ChatData(
    val chat: Chat?,
    val lastMessage: LastMessage?
)

@Serializable
data class Chat(
    val id: String,
    val title: String? = null,
    val avatar: String? = null
)

@Serializable
data class LastMessage(
    val id: String,
    val text: String? = null,
    val createdAt: String? = null,
    val user: User? = null,
)

@Serializable
data class User(
    val userId: String,
    val name: String? = null,
    val aboutMyself: String? = null,
    val avatar: String? = null
)

