package com.example.scareme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scareme.authenticationScreen.presentation.AuthenticationScreen
import com.example.scareme.signInScreen.presentation.SignInScreen
import com.example.scareme.ui.authenticationSection.AuthenticationSection
import com.example.scareme.ui.splashScreen.SplashScreen
import kotlinx.serialization.Serializable


@Composable
fun  NavigationGraph(){


    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination =  ScreenSplash
    ){
        composable<ScreenSplash> {
            SplashScreen(navController = navController)
        }
        composable<AuthenticationSections> {
            AuthenticationSection(navController = navController)
        }
        composable<SignUp> {
           AuthenticationScreen(navController = navController)
        }
        composable<SignIn> {
            SignInScreen(navController = navController)
        }

    }


}
@Serializable
object ScreenSplash
@Serializable
object AuthenticationSections
@Serializable
object SignUp
@Serializable
object SignIn
