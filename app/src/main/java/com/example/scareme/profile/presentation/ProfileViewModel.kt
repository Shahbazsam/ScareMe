package com.example.scareme.profile.presentation

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.ScareMeApplication
import com.example.scareme.TokenRepository
import com.example.scareme.profile.data.ProfileRepository
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformationToSend
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ProfileUiState{
    data class Success(val topics : List<Topics>) : ProfileUiState
    object Error : ProfileUiState
    object Loading : ProfileUiState
}
class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var profileUiState : ProfileUiState by mutableStateOf(ProfileUiState.Loading)
    var state by mutableStateOf(ProfileFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    val token = tokenRepository.getToken()

    init{
        getTopics()
    }

    fun getTopics(){
        viewModelScope.launch {
            profileUiState = ProfileUiState.Loading
            profileUiState = try {
                ProfileUiState.Success(profileRepository.getTopics(token))
            }catch (e : IOException){
                ProfileUiState.Error
            }catch (e:HttpException){
                ProfileUiState.Error
            }

        }
    }

    fun onEvent(event : ProfileEvent){
        when(event){
            is ProfileEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }
            is ProfileEvent.aboutMyselfChanged -> {
                state = state.copy(aboutMyself = event.aboutMyself)
            }
            is ProfileEvent.TopicsChanged -> {
                state = state.copy(topics = event.topics)
            }
            is ProfileEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData(){
        viewModelScope.launch {
            val userInformation = UserInformationToSend (
                name = state.name,
                aboutMyself = state.aboutMyself,
                topics = state.topics
            )
            profileRepository.updateUserProfile(token = token , userInformation = userInformation)
            validationEventChannel.send(ValidationEvent.Success)
        }


    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScareMeApplication)
                val profileRepository = application.profileContainer.profileRepository
                ProfileViewModel(
                    profileRepository = profileRepository ,
                    tokenRepository = application.tokenRepository
                    )
            }
        }
    }
    sealed class ValidationEvent{
        object Success : ValidationEvent()
    }
}