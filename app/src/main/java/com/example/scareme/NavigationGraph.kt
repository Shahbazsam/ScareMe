package com.example.scareme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scareme.authenticationScreen.presentation.AuthenticationScreen
import com.example.scareme.profile.presentation.ProfileScreen
import com.example.scareme.profile.presentation.ProfileUiState
import com.example.scareme.signInScreen.presentation.SignInScreen
import com.example.scareme.ui.authenticationSection.AuthenticationSection
import com.example.scareme.ui.splashScreen.SplashScreen
import com.example.scareme.userScreen.presentation.TinderCardWindow
import kotlinx.serialization.Serializable


@Composable
fun  NavigationGraph(
    retryAction: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){

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
        composable<ProfileInputScreen> {
            ProfileScreen(
                navController = navController,
                retryAction = retryAction,
                contentPadding = contentPadding
            )
        }
        composable<TinderNav> {
            TinderCardWindow(
                navController = navController,
                retryAction = retryAction
            )
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
@Serializable
object ProfileInputScreen
@Serializable
object TinderNav
