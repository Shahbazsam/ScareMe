package com.example.scareme.chat.data

import com.example.scareme.chat.data.model.ChatData
import com.example.scareme.chat.data.model.Messages
import com.example.scareme.chat.network.ChatApiService

interface ChatRepository {

    suspend fun getChatList(token : String) : List<ChatData>

    suspend fun getMessageList(token: String , chatId : String) : List<Messages>
}

class NetworkChatRepository(
    private val chatApiService: ChatApiService
) : ChatRepository{


    override suspend fun getChatList(token: String): List<ChatData> {
        return chatApiService.getChats(token)
    }

    override suspend fun getMessageList(token: String, chatId: String): List<Messages> {
        return chatApiService.getMessages(token, chatId)
    }
}