package com.example.scareme.chat.data

import com.example.scareme.chat.data.model.ChatData
import com.example.scareme.chat.data.model.Messages
import com.example.scareme.chat.network.ChatApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

interface ChatRepository {

    suspend fun getChatList(token : String) : List<ChatData>

    suspend fun getMessageList(token: String , chatId : String) : List<Messages>
    suspend fun sendMessages(token: String , chatId: String , text: String , avatar : MultipartBody.Part ?= null)
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

    override suspend fun sendMessages(token: String, chatId: String , text : String , avatar : MultipartBody.Part?) {
        val textRequestBody = text.toRequestBody("text/plain".toMediaTypeOrNull()) // Create RequestBody
        chatApiService.sendMessage(token, chatId, avatar, textRequestBody)
    }
}