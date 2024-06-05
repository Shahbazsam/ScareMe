package com.example.scareme.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.ScareMeApplication
import com.example.scareme.profile.data.ProfileRepository

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScareMeApplication)
                val profileRepository = application.profileContainer.profileRepository
                ProfileViewModel(profileRepository = profileRepository)
            }
        }
    }
}