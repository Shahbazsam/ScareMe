package com.example.scareme.authenticationScreen.presentation

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
import com.example.scareme.authenticationScreen.data.AuthRegisterRepository
import com.example.scareme.authenticationScreen.data.model.UserData
import com.example.scareme.authenticationScreen.domain.use_case.ValidateEmail
import com.example.scareme.authenticationScreen.domain.use_case.ValidatePassword
import com.example.scareme.authenticationScreen.domain.use_case.ValidateRepeatedPassword
import com.example.scareme.profile.presentation.ProfileViewModel
import com.example.scareme.profile.presentation.ShowProfileViewModel
import com.example.scareme.userScreen.presentation.UserViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val authRegisterRepository: AuthRegisterRepository,
    val profileViewModel: ProfileViewModel,
    val userViewModel: UserViewModel,
    val showProfileViewModel: ShowProfileViewModel
) : ViewModel() {

    var state by mutableStateOf(RegistrationFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent){
        when(event){
           is RegistrationFormEvent.EmailChanged -> {
               state = state.copy(email = event.email)
           }
            is RegistrationFormEvent.PasswordChanged ->{
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.Submit ->{
                submitData()
            }
        }
    }
    private fun submitData(){
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            state.repeatedPassword , state.password
        )
       val hasError = listOf(
           emailResult,
           passwordResult,
           repeatedPasswordResult
       ).any { !it.successful }

        if (hasError){
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage
            )
            return
        }
        if(!hasError){
            viewModelScope.launch {
                val userData = UserData(
                    email = state.email,
                    password = state.password
                )

                val token = authRegisterRepository.getRegistered(userData)

                profileViewModel.onTokenAvailable(token.accessToken)
                userViewModel.onTokenAvailable(token.accessToken)
                showProfileViewModel.onTokenAvailable(token.accessToken)
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScareMeApplication)
                val authRegisterRepository = application.container.authRegisterRepository
                AuthenticationViewModel(
                    authRegisterRepository = authRegisterRepository,
                    profileViewModel =application.profileViewModel,
                    userViewModel = application.userViewModel,
                    showProfileViewModel = application.showProfileViewModel
                    )
            }
        }
    }


    sealed class ValidationEvent {
        object Success  : ValidationEvent()

    }

}