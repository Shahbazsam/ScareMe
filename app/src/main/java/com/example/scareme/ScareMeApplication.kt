package com.example.scareme

import android.app.Application
import com.example.scareme.authenticationScreen.data.AuthAppContainer
import com.example.scareme.authenticationScreen.data.DefaultContainer
import com.example.scareme.profile.data.DefaultProfileContainer
import com.example.scareme.profile.data.ProfileContainer
import com.example.scareme.signInScreen.data.DefaultSignInContainer
import com.example.scareme.signInScreen.data.SignInAppContainer
import com.example.scareme.userScreen.data.DefaultUserContainer
import com.example.scareme.userScreen.data.UserContainer


class ScareMeApplication : Application() {

    lateinit var container : AuthAppContainer
    lateinit var container2 : SignInAppContainer
    lateinit var profileContainer : ProfileContainer
    lateinit var userContainer: UserContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer()
        container2 = DefaultSignInContainer()
        profileContainer = DefaultProfileContainer()
        userContainer = DefaultUserContainer()
    }
}