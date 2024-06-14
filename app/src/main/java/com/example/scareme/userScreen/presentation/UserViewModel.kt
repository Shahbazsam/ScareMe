package com.example.scareme.userScreen.presentation

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.SaveTokenUtil
import com.example.scareme.ScareMeApplication
import com.example.scareme.TokenRepository
import com.example.scareme.userScreen.data.UserRepository
import com.example.scareme.userScreen.data.model.UserData
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface UserUiState {
    data class Success(val user: List<UserData>) : UserUiState
    object Error : UserUiState
    object Loading : UserUiState
}

class UserViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var userUiState: UserUiState by mutableStateOf(UserUiState.Loading)
        private set

    private val token : String
        get() =  tokenRepository.getToken()

    init {
        getUserDetails()
    }

    fun likeUser(userId: String) {
        viewModelScope.launch {

                userRepository.likeUser(token , userId)
        }
    }

    fun dislikeUser(userId: String) {
        viewModelScope.launch {

                userRepository.dislikeUser(token, userId)

        }
    }


    fun getUserDetails(){
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            userUiState = try {
                UserUiState.Success(userRepository.getUser(token ))
            } catch (e: IOException) {
                UserUiState.Error
            } catch (e: HttpException) {
                UserUiState.Error
            }

        }
    }


    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScareMeApplication)
                val userRepository = application.userContainer.userRepository
                UserViewModel(
                    userRepository = userRepository,
                    tokenRepository = application.tokenRepository
                    )
            }
        }
    }
}