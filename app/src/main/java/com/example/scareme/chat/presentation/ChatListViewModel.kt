package com.example.scareme.chat.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.ScareMeApplication
import com.example.scareme.chat.data.ChatRepository
import com.example.scareme.chat.data.model.ChatData
import com.example.scareme.chat.data.model.Messages
import com.example.scareme.chat.data.model.PhotoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface ChatUiState {
    data class Success(val chat: List<ChatData>) : ChatUiState
    object Error : ChatUiState
    object Loading : ChatUiState
}

sealed interface MessageUiState {
    data class Success(val message: List<Messages>, val data: PhotoData ) : MessageUiState
    object Error : MessageUiState
    object Loading : MessageUiState
}



class ChatListViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    var state by mutableStateOf(MessageState())

    private val _chatUiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val chatUiState = _chatUiState.asStateFlow()

    private val _messageUiState = MutableStateFlow<MessageUiState>(MessageUiState.Loading)
    val messageUiState = _messageUiState.asStateFlow()


    private var token: String = "" // Store the token
    fun onTokenAvailable(token: String) {
        this.token = token
        viewModelScope.launch {
            getChatList()
        }
    }
    fun onEvent(event: MessageEvent){
        when(event){
            is MessageEvent.MessageChanged -> {
                state = state.copy(message = event.message)
            }
            is MessageEvent.submit -> {
                sendMessages(state.message)
            }
        }
    }
    private var chatId : String = ""
    private var name : String? = ""
    private var avatar : String? = ""

    fun onCall(chatId : String , name : String? , avatar :String?  ){
        this.chatId = chatId
        this.name = name
        this.avatar = avatar
        getMessages()
    }


    fun getMessages(){
        viewModelScope.launch {
            _messageUiState.value = MessageUiState.Loading
            try {
                val response =  chatRepository.getMessageList("Bearer $token" , chatId)
                _messageUiState.value = MessageUiState.Success(response , data =
                    PhotoData(
                        id = chatId , name = name, avatar =  avatar
                    )
                )
            } catch (e: IOException) {
                _messageUiState.value = MessageUiState.Error
            } catch (e: HttpException) {
                _messageUiState.value = MessageUiState.Error
            }
        }
    }
    fun sendMessages(message : String){
        viewModelScope.launch {
            chatRepository.sendMessages("Bearer $token", chatId , message , avatar = null )
            getMessages()
        }
    }

    fun getChatList(){
        viewModelScope.launch {
            _chatUiState.value = ChatUiState.Loading
            try {
                val response = chatRepository.getChatList("Bearer $token")
                _chatUiState.value = ChatUiState.Success(response)
            } catch (e: IOException) {
                _chatUiState.value = ChatUiState.Error
            } catch (e: HttpException) {
                _chatUiState.value = ChatUiState.Error
            }

        }
    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScareMeApplication)
                val chatRepository = application.chatContainer.chatRepository
                ChatListViewModel(
                    chatRepository = chatRepository
                )
            }
        }
    }


}