package com.example.scareme

import android.app.Application
import com.example.scareme.authenticationScreen.data.AuthAppContainer
import com.example.scareme.authenticationScreen.data.DefaultContainer
import com.example.scareme.chat.data.ChatContainer
import com.example.scareme.chat.data.DefaultChatContainer
import com.example.scareme.chat.presentation.ChatListViewModel
import com.example.scareme.profile.data.DefaultProfileContainer
import com.example.scareme.profile.data.ProfileContainer
import com.example.scareme.profile.presentation.ProfileViewModel
import com.example.scareme.profile.presentation.ShowProfileViewModel
import com.example.scareme.signInScreen.data.DefaultSignInContainer
import com.example.scareme.signInScreen.data.SignInAppContainer
import com.example.scareme.userScreen.data.DefaultUserContainer
import com.example.scareme.userScreen.data.UserContainer
import com.example.scareme.userScreen.presentation.UserViewModel


class ScareMeApplication : Application() {

    lateinit var container : AuthAppContainer
    lateinit var container2 : SignInAppContainer
    lateinit var profileContainer : ProfileContainer
    lateinit var userContainer: UserContainer
    lateinit var chatContainer: ChatContainer

    lateinit var profileViewModel: ProfileViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var showProfileViewModel: ShowProfileViewModel
    lateinit var chatListViewModel: ChatListViewModel
    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer()
        container2 = DefaultSignInContainer()
        profileContainer = DefaultProfileContainer()
        userContainer = DefaultUserContainer()
        chatContainer = DefaultChatContainer()

        profileViewModel = ProfileViewModel(profileContainer.profileRepository , context = this)
        userViewModel = UserViewModel(userContainer.userRepository )
        showProfileViewModel = ShowProfileViewModel(profileContainer.profileRepository)
        chatListViewModel = ChatListViewModel(chatContainer.chatRepository)
    }
}