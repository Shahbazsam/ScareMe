package com.example.scareme.profile.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.ScareMeApplication
import com.example.scareme.profile.data.ProfileRepository
import com.example.scareme.profile.data.model.UserInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
var USERID = ""
sealed interface ShowProfileUiState{
    data class Success(val userInformation  : UserInformation) : ShowProfileUiState
    object Error : ShowProfileUiState
    object Loading : ShowProfileUiState
}

class ShowProfileViewModel(
    private val profileRepository: ProfileRepository,

) :ViewModel(){

    private val _showProfileUiState = MutableStateFlow<ShowProfileUiState>(ShowProfileUiState.Loading)
    val showProfileUiState = _showProfileUiState.asStateFlow()

   private var token : String = ""

    fun onTokenAvailable(token : String){
        this.token = token
        viewModelScope.launch { getUserInformation() }

    }

    fun getUserInformation(){
        viewModelScope.launch {
            _showProfileUiState.value = ShowProfileUiState.Loading
             try {
                val userInfo = profileRepository.getUserProfile("Bearer $token")
                 USERID = userInfo.userId
                _showProfileUiState.value = ShowProfileUiState.Success(userInfo)
                 Log.d("ShowProfileViewModel", "State updated to Success: $userInfo") // Add logging
            }catch (e : IOException){
                _showProfileUiState.value = ShowProfileUiState.Error
            }catch (e: HttpException){
                 _showProfileUiState.value = ShowProfileUiState.Error
            }
        }
    }



    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScareMeApplication)
                val profileRepository = application.profileContainer.profileRepository
                ShowProfileViewModel(
                    profileRepository = profileRepository,

                    )
            }
        }
    }

}