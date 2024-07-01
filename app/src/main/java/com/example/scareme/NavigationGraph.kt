package com.example.scareme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.scareme.authenticationScreen.presentation.AuthenticationScreen
import com.example.scareme.chat.presentation.ChatListScreen
import com.example.scareme.chat.presentation.MessagingScreen
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

    val selectedItemIndex = NavigationState.selectedItemIndex
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
        composable<MessageNav> {

            MessagingScreen(
                viewModel = app.chatListViewModel,
                retryAction = {}
            )
        }
        composable<TinderNav> {
            Scaffold (
                bottomBar = {
                    BottomNavigationBar (
                        currentSelectedItemIndex = selectedItemIndex,
                        onItemClick = { clickedIndex -> NavigationState.selectedItemIndex = clickedIndex
                            when(clickedIndex){
                                0 -> {
                                    app.userViewModel.getUserDetails()
                                    navController.navigate(TinderNav)
                                }
                                1->{
                                    app.chatListViewModel.getChatList()
                                    navController.navigate(ChatListNav)
                                }
                                2 -> {
                                    app.showProfileViewModel.getUserInformation()
                                    navController.navigate(ShowProfileNav)
                                }
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
                        currentSelectedItemIndex = selectedItemIndex,
                        onItemClick = { clickedIndex -> NavigationState.selectedItemIndex = clickedIndex
                            when(clickedIndex){
                                0 -> {
                                    app.userViewModel.getUserDetails()
                                    navController.navigate(TinderNav)
                                    }
                                1->{
                                    app.chatListViewModel.getChatList()
                                    navController.navigate(ChatListNav)
                                }
                                2 -> {
                                    app.showProfileViewModel.getUserInformation()
                                    navController.navigate(ShowProfileNav)
                                    }
                            }
                        }
                    )
                }
            ){innerPadding ->
               ShowProfileScreen(
                   viewModel = app.showProfileViewModel,
                   retryAction = {} ,
                   modifier = Modifier.padding(innerPadding)
               )

            }
        }
        composable<ChatListNav> {
            Scaffold (
                bottomBar = {
                    BottomNavigationBar (
                        currentSelectedItemIndex = selectedItemIndex,
                        onItemClick = { clickedIndex -> NavigationState.selectedItemIndex  = clickedIndex
                            when(clickedIndex){
                                0 -> {
                                    app.userViewModel.getUserDetails()
                                    navController.navigate(TinderNav)
                                }
                                1->{
                                    app.chatListViewModel.getChatList()
                                    navController.navigate(ChatListNav)
                                }
                                2 -> {
                                    app.showProfileViewModel.getUserInformation()
                                    navController.navigate(ShowProfileNav)
                                }
                            }
                        }
                    )
                }
            ){innerPadding ->
                ChatListScreen(
                    viewModel = app.chatListViewModel,
                    retryAction = {  },
                    navController = navController,
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
@Serializable
object ChatListNav
@Serializable
object MessageNav

object NavigationState {
    var selectedItemIndex by mutableStateOf(0)
}