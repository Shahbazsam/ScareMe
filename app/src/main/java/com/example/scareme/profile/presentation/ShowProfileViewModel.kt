package com.example.scareme.profile.presentation

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
import com.example.scareme.profile.data.ProfileRepository
import com.example.scareme.profile.data.model.UserInformation
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ShowProfileUiState{
    data class Success(val userInformation  : List<UserInformation>) : ShowProfileUiState
    object Error : ShowProfileUiState
    object Loading : ShowProfileUiState
}

class ShowProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val application: Application,
) :ViewModel(){

    var showProfileUiState : ShowProfileUiState by mutableStateOf(ShowProfileUiState.Loading)


    private fun getToken(): String {
        val sharedPref = application.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)?: throw IllegalStateException("Token not found in SharedPreferences")
    }
    val token = getToken()

    init {
        getUserInformation()
    }

    fun getUserInformation(){
        viewModelScope.launch {
            showProfileUiState = ShowProfileUiState.Loading
            showProfileUiState = try {
                val userInfo = profileRepository.getUserProfile(token)
                ShowProfileUiState.Success(userInfo)
            }catch (e : IOException){
                ShowProfileUiState.Error
            }catch (e: HttpException){
                ShowProfileUiState.Error
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
                    application = application
                    )
            }
        }
    }

}