package com.example.scareme.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.ScareMeApplication
import com.example.scareme.chat.data.ChatRepository
import com.example.scareme.chat.data.model.ChatData
import com.example.scareme.userScreen.data.model.UserData
import com.example.scareme.userScreen.presentation.UserUiState
import com.example.scareme.userScreen.presentation.UserViewModel
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

class ChatListViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatUiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val chatUiState = _chatUiState.asStateFlow()

    private var token: String = "" // Store the token
    fun onTokenAvailable(token: String) {
        this.token = token
        viewModelScope.launch {
            getChatList()
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