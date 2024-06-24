package com.example.scareme.profile.presentation

import android.net.Uri
import com.example.scareme.authenticationScreen.presentation.RegistrationFormEvent

sealed class ProfileEvent {
    data class NameChanged(val name: String) : ProfileEvent()
    data class aboutMyselfChanged(val aboutMyself: String) : ProfileEvent()
    data class TopicsSelected(val topics: String) : ProfileEvent()
    data class ImageSelected(val uri : Uri):ProfileEvent()
    object Submit : ProfileEvent()
}
