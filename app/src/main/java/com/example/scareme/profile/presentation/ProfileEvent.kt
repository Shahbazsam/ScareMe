package com.example.scareme.profile.presentation

import com.example.scareme.authenticationScreen.presentation.RegistrationFormEvent

sealed class ProfileEvent {
    data class NameChanged(val name: String) : ProfileEvent()
    data class aboutMyselfChanged(val aboutMyself: String) : ProfileEvent()
    data class TopicsChanged(val topics: List<String>) : ProfileEvent()

    object Submit : ProfileEvent()
}
