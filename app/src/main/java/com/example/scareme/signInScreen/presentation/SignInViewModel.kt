package com.example.scareme.signInScreen.presentation

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
import com.example.scareme.authenticationScreen.presentation.AuthenticationViewModel
import com.example.scareme.chat.presentation.ChatListViewModel
import com.example.scareme.profile.presentation.ShowProfileViewModel
import com.example.scareme.signInScreen.data.SignInRepository
import com.example.scareme.signInScreen.data.model.UserData
import com.example.scareme.signInScreen.domain.use_case.ValidateEmail
import com.example.scareme.signInScreen.domain.use_case.ValidatePassword
import com.example.scareme.userScreen.presentation.UserViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val signInRepository: SignInRepository,
    val userViewModel: UserViewModel,
    val showProfileViewModel: ShowProfileViewModel,
    val chatListViewModel: ChatListViewModel

) : ViewModel() {

    var state by mutableStateOf(SignInFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: SignInFormEvent){
        when(event){
           is SignInFormEvent.EmailChanged -> {
               state = state.copy(email = event.email)
           }
            is SignInFormEvent.PasswordChanged ->{
                state = state.copy(password = event.password)
            }

            is SignInFormEvent.Submit ->{
                submitData()
            }
        }
    }
    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)

       val hasError = listOf(
           emailResult,
           passwordResult,
       ).any { !it.successful }

        if (hasError){
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
            )
            return
        }
        if(!hasError){
            viewModelScope.launch {
                try {
                    val userData = UserData(
                        email = state.email,
                        password = state.password
                    )

                    val token = signInRepository.getSignedIn(userData)
                    userViewModel.onTokenAvailable(token.accessToken)
                    showProfileViewModel.onTokenAvailable(token.accessToken)
                    chatListViewModel.onTokenAvailable(token.accessToken)
                    validationEventChannel.send(ValidationEvent.Success)
                }catch (e: Exception) {
                    validationEventChannel.send(ValidationEvent.Error(e.message ?: "Registration failed"))
                }

            }
        }

    }
    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScareMeApplication)
                val signInRepository = application.container2.signInRepository
                SignInViewModel(
                    signInRepository = signInRepository,
                    userViewModel = application.userViewModel,
                    showProfileViewModel = application.showProfileViewModel,
                    chatListViewModel = application.chatListViewModel
                    )
            }
        }
    }


    sealed class ValidationEvent {
        object Success  : ValidationEvent()
        data class Error(val message: String) : ValidationEvent()
    }

}