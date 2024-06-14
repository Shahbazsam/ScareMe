package com.example.scareme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scareme.authenticationScreen.presentation.AuthenticationScreen
import com.example.scareme.profile.presentation.ProfileScreen
import com.example.scareme.profile.presentation.ProfileUiState
import com.example.scareme.profile.presentation.ProfileViewModel
import com.example.scareme.profile.presentation.ShowProfileScreen
import com.example.scareme.profile.presentation.ShowProfileViewModel
import com.example.scareme.signInScreen.presentation.SignInScreen
import com.example.scareme.ui.authenticationSection.AuthenticationSection
import com.example.scareme.ui.splashScreen.SplashScreen
import com.example.scareme.userScreen.presentation.TinderCardWindow
import com.example.scareme.userScreen.presentation.UserUiState
import com.example.scareme.userScreen.presentation.UserViewModel
import kotlinx.serialization.Serializable


@Composable
fun  NavigationGraph(

    app: ScareMeApplication
){

val contentPadding  = PaddingValues(0.dp)
    val navController = rememberNavController()
    val userViewModel : UserViewModel = viewModel(factory = UserViewModel.Factory)
    val profileViewModel : ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
   val showProfileViewModel : ShowProfileViewModel = viewModel(factory = ShowProfileViewModel.Factory)
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
                retryAction = {},
                viewModel = app.profileViewModel,
                contentPadding = contentPadding
            )
        }
        composable<TinderNav> {
            Scaffold (
                bottomBar = {
                    BottomNavigationBar (
                        onItemClick = { clickedIndex ->
                            when(clickedIndex){
                                0 -> navController.navigate(TinderNav)
                                2 -> navController.navigate(ShowProfileNav)
                            }
                        }
                    )
                }
            ){innerPadding ->
                TinderCardWindow(
                    navController = navController ,
                    viewModel = app.userViewModel,
                    retryAction = userViewModel::getUserDetails,
                    modifier = Modifier.padding(innerPadding)
                )

            }
        }
        composable<ShowProfileNav> {
            Scaffold (
                bottomBar = {
                    BottomNavigationBar (
                        onItemClick = { clickedIndex ->
                            when(clickedIndex){
                                0 -> navController.navigate(TinderNav)
                                2 -> navController.navigate(ShowProfileNav)
                            }
                        }
                    )
                }
            ){innerPadding ->
               ShowProfileScreen(
                   retryAction = {} ,
                   modifier = Modifier.padding(innerPadding)
               )

            }
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

@Serializable
object ShowProfileNav
