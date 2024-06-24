package com.example.scareme.userScreen.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import kotlinx.coroutines.launch


@Composable
fun TinderCardWindow(
    navController: NavController,
    viewModel: UserViewModel,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.userUiState.collectAsState()

    val userUiState = uiState

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(vertical = 23.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
       // val profiles = listOf<MatchProfile>() // Replace with your data source
        val states = profiles.reversed()
            .map { it to rememberSwipeableCardState() }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = 400.dp, height = 370.dp),
                painter = painterResource(id = R.drawable.background),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Trick or Treat?",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(x = 21.dp, y = 47.dp)
                        .padding(start = 12.dp, bottom = 12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                val scope = rememberCoroutineScope()
                user?.let {
                    Box(
                        modifier = Modifier
                            .offset(y = 50.dp)
                            .size(height = 508.dp, width = 318.dp)
                            .align(Alignment.CenterHorizontally)
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
                                            viewModel.dislikeUser(user.userId)
                                            // Send to dislike API
                                        }

                                        Direction.Right -> {
                                            viewModel.likeUser(user.userId)
                                            // Send to like API
                                        }

                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
                //Spacer(modifier = Modifier.padding(12.dp))
                Row(
                    Modifier
                        .padding(horizontal = 31.dp)
                        .offset(y=57.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        icon = painterResource(id = R.drawable.dislike)
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
                        icon = painterResource(id = R.drawable.like)
                    )
                }
            }
        }
    }
}

@Composable
fun CircleButton(
    onClick: () -> Unit,
    icon: Painter,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .clip(CircleShape)
            .size(70.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Transparent background
            contentColor = Color.Transparent // Transparent ripple
        )
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = icon,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

    }
   /* IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xff401c34))
            .size(56.dp)
            .border(2.dp, Color.Black, CircleShape),
        onClick = onClick
    ) {
        Icon(
            painter = icon,
            null,
            modifier = Modifier.size(54.dp)
        )
    }*/
}

@Composable
fun ProfileCard(
    modifier: Modifier,
    name: String?,
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
                if (name != null) {
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
}



@Preview(showBackground = true)
@Composable
fun PreviewSwipeUserScreen() {
   // SwipeUserScreen()
}




