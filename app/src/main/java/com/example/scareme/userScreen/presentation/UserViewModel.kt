package com.example.scareme.userScreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.ScareMeApplication
import com.example.scareme.userScreen.data.UserRepository
import com.example.scareme.userScreen.data.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {

    private val _userUiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val userUiState = _userUiState.asStateFlow()

    private var token: String = "" // Store the token
    fun onTokenAvailable(token: String) {
        this.token = token
        viewModelScope.launch {
            getUserDetails()
        }
    }

    fun likeUser(userId: String) {
        viewModelScope.launch {

                userRepository.likeUser("Bearer $token" , userId)
                getUserDetails()
        }
    }

    fun dislikeUser(userId: String) {
        viewModelScope.launch {

                userRepository.dislikeUser("Bearer $token", userId)
                getUserDetails()

        }
    }


    fun getUserDetails(){
        viewModelScope.launch {
            _userUiState.value = UserUiState.Loading
             try {
                val response = userRepository.getUser("Bearer $token" )
                _userUiState.value = UserUiState.Success(response)
            } catch (e: IOException) {
                _userUiState.value = UserUiState.Error
            } catch (e: HttpException) {
                _userUiState.value = UserUiState.Error
            }

        }
    }


    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScareMeApplication)
                val userRepository = application.userContainer.userRepository
                UserViewModel(
                    userRepository = userRepository
                    )
            }
        }
    }
}