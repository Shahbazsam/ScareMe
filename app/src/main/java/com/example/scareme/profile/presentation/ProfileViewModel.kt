package com.example.scareme.profile.presentation

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
import com.example.scareme.profile.data.ProfileRepository
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformationToSend
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

) : ViewModel() {

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState = _profileUiState.asStateFlow()
    var state by mutableStateOf(ProfileFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private var token: String = "" // Store the token

    fun onTokenAvailable(token: String) {
        this.token = token
        viewModelScope.launch {
            getTopics()
        }
    }

    //val token = tokenRepository.getToken()
     suspend fun getTopics(){
        viewModelScope.launch {
            _profileUiState.value = ProfileUiState.Loading
            try {
                val response = profileRepository.getTopics(token)
                _profileUiState.value = ProfileUiState.Success(response)

            }catch (e : IOException){
                _profileUiState.value = ProfileUiState.Error
            }catch (e:HttpException){
                _profileUiState.value=  ProfileUiState.Error
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

    private fun submitData() {
        viewModelScope.launch {
            val userInformation = UserInformationToSend(
                name = state.name,
                aboutMyself = state.aboutMyself,
                topics = state.topics
            )
            try {
                    profileRepository.updateUserProfile(token = "Bearer $token", userInformation = userInformation)
                validationEventChannel.send(ValidationEvent.Success)
            } catch (e: Exception) {
                // Handle error, e.g., show an error message
                println("Error updating profile: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScareMeApplication)
                val profileRepository = application.profileContainer.profileRepository
                ProfileViewModel(
                    profileRepository = profileRepository,
                )
            }
        }
    }
    sealed class ValidationEvent{
        object Success : ValidationEvent()
    }
}