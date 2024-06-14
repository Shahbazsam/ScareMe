package com.example.scareme.userScreen.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.example.scareme.R
import com.example.scareme.userScreen.data.model.UserData
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@Composable
fun TinderCardWindow(
    navController: NavController,
    viewModel: UserViewModel,
    userUiState: UserUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){

    when(userUiState){
        is UserUiState.Loading -> UserLoadingScreen(modifier = modifier.fillMaxSize())
        is UserUiState.Success -> SwipeUserScreen(
            userInformation =  userUiState.user, viewModel = viewModel ,  modifier = modifier.fillMaxWidth()
        )
        is UserUiState.Error -> UserErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }

}
@Composable
fun UserLoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = null
    )
}

@Composable
fun UserErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = ("Loading Failed"),
            modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}



@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun SwipeUserScreen(
    viewModel: UserViewModel,
    userInformation: List<UserData>,
    modifier: Modifier = Modifier
) {
    val user = userInformation.firstOrNull()
    TransparentSystemBars()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
       // val profiles = listOf<MatchProfile>() // Replace with your data source
        val states = profiles.reversed()
            .map { it to rememberSwipeableCardState() }

        // Background Image
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(width = 400.dp, height = 370.dp),
            painter = painterResource(id = R.drawable.background),
            contentDescription = null
        )

        val scope = rememberCoroutineScope()
       user?.let {
           Box(
               Modifier
                   .padding(18.dp)
                   .fillMaxSize()
                   .aspectRatio(1f)
                   .align(Alignment.Center)
           ) {
               states.forEach { (it, state) ->
                   if (state.swipedDirection == null) {
                       ProfileCard(
                           modifier = Modifier
                               .fillMaxSize()
                               .swipableCard(
                                   state = state,
                                   blockedDirections = listOf(Direction.Down),
                                   onSwiped = {
                                       // Handle swipes
                                   },
                                   onSwipeCancel = {
                                       Log.d("Swipeable-Card", "Cancelled swipe")
                                   }
                               ),
                           name = user.name,
                           avatar = user.avatar
                       )
                   }

                   LaunchedEffect(it, state.swipedDirection) {
                       if (state.swipedDirection != null) {
                           // Handle swipe action here, e.g., send user ID to the API
                           when (state.swipedDirection) {
                               Direction.Left -> {
                                   viewModel.likeUser(user.userId)
                                   // Send to dislike API
                               }

                               Direction.Right -> {
                                   viewModel.dislikeUser(user.userId)
                                   // Send to like API
                               }

                               else -> {}
                           }
                       }
                   }
               }
           }
       }

        // Buttons for swiping programmatically
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CircleButton(
                onClick = {
                    scope.launch {
                        val last = states.reversed()
                            .firstOrNull {
                                it.second.offset.value == Offset(0f, 0f)
                            }?.second
                        last?.swipe(Direction.Left)
                    }
                },
                icon = Icons.Rounded.Close
            )
            CircleButton(
                onClick = {
                    scope.launch {
                        val last = states.reversed()
                            .firstOrNull {
                                it.second.offset.value == Offset(0f, 0f)
                            }?.second
                        last?.swipe(Direction.Right)
                    }
                },
                icon = Icons.Rounded.Favorite
            )
        }
    }
}

@Composable
fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Red)
            .size(56.dp)
            .border(2.dp, Color.Black, CircleShape),
        onClick = onClick
    ) {
        Icon(icon, null, tint = Color.Black)
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier,
    name: String,
    avatar: String?,
) {
    Card(modifier) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(avatar)
                    .crossfade(true).build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Column(Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
            isNavigationBarContrastEnforced = false
        )
        onDispose {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSwipeUserScreen() {
   // SwipeUserScreen()
}




