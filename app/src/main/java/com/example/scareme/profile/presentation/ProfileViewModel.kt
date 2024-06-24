package com.example.scareme.profile.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scareme.R
import com.example.scareme.ScareMeApplication
import com.example.scareme.profile.data.ProfileRepository
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformationToSend
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

sealed interface ProfileUiState {
    data class Success(val topics: List<Topics>) : ProfileUiState
    object Error : ProfileUiState
    object Loading : ProfileUiState
}

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val context: Context
) : ViewModel() {

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState = _profileUiState.asStateFlow()
    var state by mutableStateOf(ProfileFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val errorEventChannel = Channel<String>()
    val errorEvents = errorEventChannel.receiveAsFlow() // Expose error events

    var selectedImageUri: Uri? = null
    private var token: String = "" // Store the token

    fun onTokenAvailable(token: String) {
        this.token = token
        viewModelScope.launch {
            getTopics()
        }
    }

    suspend fun getTopics() {
        viewModelScope.launch {
            _profileUiState.value = ProfileUiState.Loading
            try {
                val response = profileRepository.getTopics(token)
                _profileUiState.value = ProfileUiState.Success(response)

            } catch (e: IOException) {
                _profileUiState.value = ProfileUiState.Error
                errorEventChannel.send("Network Error: ${e.message}")
            } catch (e: HttpException) {
                _profileUiState.value = ProfileUiState.Error
                errorEventChannel.send("API Error: ${e.message}")
            }

        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }
            is ProfileEvent.aboutMyselfChanged -> {
                state = state.copy(aboutMyself = event.aboutMyself)
            }
            is ProfileEvent.TopicsSelected ->  {
                // Modify the selected topic IDs list in the state
                val updatedTopics = state.topics?.toMutableList()
                if (updatedTopics != null) {
                    if (updatedTopics.contains(event.topics)) {
                        updatedTopics.remove(event.topics) // Deselect if already selected
                    } else {
                        updatedTopics.add(event.topics) // Select
                    }
                }
                state = state.copy(topics = updatedTopics)
            }
            is ProfileEvent.ImageSelected -> {
                selectedImageUri = event.uri
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
                profileRepository.updateUserProfile(
                    token = "Bearer $token",
                    userInformation = userInformation
                )

                if (selectedImageUri != null) {
                    uploadImage(selectedImageUri!!) // Upload selected image
                } else {
                    uploadDefaultImage() // Upload default image
                }

                validationEventChannel.send(ValidationEvent.Success)
            } catch (e: Exception) {
                // Handle error, e.g., show an error message
                errorEventChannel.send("Error updating profile: ${e.message}")
            }
        }
    }

    private suspend fun uploadImage(imageUri: Uri) {
        val imageFile = imageUri.toFile(context)
        val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("avatar", imageFile.name, requestBody)

        try {
            profileRepository.updateAvatar(token = "Bearer $token", uri = imagePart)
        } catch (e: Exception) {
            errorEventChannel.send("Image Upload Failed: ${e.message}")
        }
    }

    private suspend fun uploadDefaultImage() {
        val defaultImageDrawable =
            ResourcesCompat.getDrawable(context.resources, R.drawable.profile, null)

        // Convert to Bitmap
        val defaultImageBitmap = defaultImageDrawable?.toBitmap()

        if (defaultImageBitmap != null) {
            val imageFile = bitmapToFile(context, defaultImageBitmap, "default_avatar.jpg")
            val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("avatar", imageFile.name, requestBody)
            profileRepository.updateAvatar(token = "Bearer $token", uri = imagePart)
        } else {
            // Handle the case where converting the Drawable to Bitmap fails
            errorEventChannel.send("Failed to load default image.")
        }
    }

    private fun bitmapToFile(context: Context, bitmap: Bitmap, fileNameToSave: String): File {
        val file = File(context.cacheDir, fileNameToSave)
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
    }


    private fun Uri.toFile(context: Context): File {
        val contentResolver = context.contentResolver
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val cacheFile = File(context.cacheDir, fileName)

        contentResolver.openInputStream(this)?.use { inputStream ->
            cacheFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return cacheFile
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScareMeApplication)
                val profileRepository = application.profileContainer.profileRepository
                ProfileViewModel(
                    profileRepository = profileRepository,
                    context = application.applicationContext
                )
            }
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}