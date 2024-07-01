package com.example.scareme.chat.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scareme.MessageNav
import com.example.scareme.R
import com.example.scareme.chat.data.model.ChatData

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel,
    retryAction: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.chatUiState.collectAsState()

    val chatUiState = uiState

    when(chatUiState){
        is ChatUiState.Loading -> ChatLoadingScreen(modifier = modifier.fillMaxSize())
        is ChatUiState.Success -> ChatListScreenImpl(
            chatInformation =  chatUiState.chat,navController = navController , viewModel = viewModel ,  modifier = modifier.fillMaxWidth()
        )
        is ChatUiState.Error -> ChatErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }

}

@Composable
fun ChatLoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = null
    )
}

@Composable
fun ChatErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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


@Composable
fun ChatListScreenImpl(
    viewModel: ChatListViewModel,
    navController: NavController,
    chatInformation : List<ChatData>,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(16.dp)
    ) {
        Text(
            text ="Last",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            items(chatInformation){chatData ->
                RowChatItem(chatData = chatData)
            }
        }
        Text(
            text ="Messages",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 34.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            items(chatInformation){chatData ->
                VerticalChatItem(chatData = chatData , navController = navController , viewModel = viewModel)
            }

        }
    }
}

@Composable
fun RowChatItem(chatData: ChatData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(chatData.chat?.avatar)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clickable {  }
                .size(64.dp)
                .clip(CircleShape)
        )
        chatData.chat?.title?.let {
            Text(
                text = it,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // ... [Rest of your chat item layout - you'll likely need to adjust 
        //     how you display data from chatData]
    }
}

@Composable
fun VerticalChatItem(
    viewModel: ChatListViewModel,
    navController: NavController,
    chatData: ChatData
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                chatData.chat?.let { viewModel.onCall(it.id, it?.title , it?.avatar) }
                navController.navigate(MessageNav)
            }
            .fillMaxWidth()
    ){
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(chatData.chat?.avatar)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column {
            chatData.chat?.title?.let {
                Text(

                    text = it,

                    )
            }
            chatData.lastMessage?.text?.let {
                Text(
                    text = it,
                    color = Color.White,
                    // fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFB14623))
            )

        }

    }

}


/*@Preview(showBackground = true)
@Composable
fun ChatListScreenTheme(){
    ChatListScreen()
}*/


data class UserData(
    @DrawableRes
    val photo : Int,
    val name : String,
    val lastMessage : String
)

