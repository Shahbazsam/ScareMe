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
import com.example.scareme.ScareMeApplication
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
    private val application: Application
) : ViewModel() {

    var userUiState: UserUiState by mutableStateOf(UserUiState.Loading)
        private set

    private var _token: String? = null
    val token: String
        get() {
            if (_token == null) {
                _token = findToken()
            }
            return _token ?: throw IllegalStateException("Token not found in SharedPreferences")
        }

    init {
        viewModelScope.launch {
            _token = findToken()
            if (_token != null) {
                getUserDetails()
            } else {
                userUiState = UserUiState.Error // or navigate to login
            }
        }
    }
    private fun findToken(): String? {
        val sharedPref = application.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }


    fun likeUser(userId: String) {
        viewModelScope.launch {
            token.let {
                userRepository.likeUser(it, userId)
            }
        }
    }

    fun dislikeUser(userId: String) {
        viewModelScope.launch {
            token.let {
                userRepository.dislikeUser(it, userId)
            }
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
                    application = application
                    )
            }
        }
    }
}