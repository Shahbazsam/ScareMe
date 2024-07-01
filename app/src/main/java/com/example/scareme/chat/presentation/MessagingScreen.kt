package com.example.scareme.chat.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scareme.R
import com.example.scareme.chat.data.model.Messages
import com.example.scareme.chat.data.model.PhotoData
import com.example.scareme.profile.presentation.USERID
import com.example.scareme.ui.theme.textColor
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Message(val text: String, val isUser: Boolean)

@Composable
fun MessagingScreen(
    viewModel: ChatListViewModel,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.messageUiState.collectAsState()

    val messageUiState = uiState

    when(messageUiState){
        is MessageUiState.Loading -> ChatLoadingScreen(modifier = modifier.fillMaxSize())
        is MessageUiState.Success -> MessagingScreenimpl(
            messageInformation =  messageUiState.message , photoData = messageUiState.data,  viewModel = viewModel ,  modifier = modifier.fillMaxWidth()
        )
        is MessageUiState.Error -> ChatErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreenimpl(
    viewModel: ChatListViewModel,
    messageInformation: List<Messages>,
    photoData: PhotoData,
    modifier: Modifier = Modifier
){

    val state = viewModel.state
    val fruits = listOf("Queens", "Royalty", "Knives", "Date", "Vampires")

    Column (
       modifier = Modifier
           .background(Color.Black)
           .fillMaxSize()
   ){
       Spacer(modifier = Modifier
           .height(110.dp)
       )
       Row (modifier = Modifier.fillMaxWidth(1f)){

           AsyncImage(
               model = ImageRequest.Builder(context = LocalContext.current)
                   .data(photoData.avatar)
                   .crossfade(true)
                   .build(),
               error = painterResource(R.drawable.ic_broken_image),
               placeholder = painterResource(R.drawable.loading_img),
               contentDescription = null,
               contentScale = ContentScale.Crop,
               modifier = Modifier
                   .offset(x = 15.dp)
                   .size(104.dp)
                   .clip(CircleShape),
           )
           /*Image(
               modifier = Modifier
                   .offset(x = 15.dp)
                   .size(104.dp)
                   .clip(CircleShape),
               painter = painterResource(id = R.drawable.barker),
               contentDescription = null
           )*/
           Spacer(modifier = Modifier
                    .width(30.dp)
           )
           Column {

                   photoData.name?.let {
                       Text(
                           text = it,
                           color = Color.White,
                           fontSize = 38.sp,
                           fontWeight = FontWeight.ExtraBold,
                           textAlign = TextAlign.Center,
                           modifier = Modifier

                       )
                   }

               Spacer(modifier = Modifier.padding(10.dp))
               ShowTopics(topics  =  fruits)
           }
       }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(390.dp),
            contentPadding = PaddingValues(16.dp), // Add padding around messages
            reverseLayout = true // Reverse the order to show latest at the bottom
        ) {
            items(messageInformation) { message ->
                MessageBubble(message)
            }

        }
        Spacer(modifier = Modifier.height(19.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(
            value = state.message ,
            onValueChange = {
                            viewModel.onEvent(MessageEvent.MessageChanged(it))
            },
            modifier = Modifier
                .offset(x = 10.dp)
                .size(width = 280.dp, height = 57.dp)
                .fillMaxWidth()
                .align(Alignment.Bottom),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = textColor,
                focusedBorderColor = textColor,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                focusedPlaceholderColor = Color.White,
                containerColor = textColor
            )
        )
        Spacer(modifier = Modifier.width(34.dp))

            Button(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(57.dp),
               // shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Transparent background
                    contentColor = Color.Transparent
                ),
                onClick = { viewModel.onEvent(MessageEvent.submit) }
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

            }
           // Text("Send", color = Color.Black, fontWeight = FontWeight.Bold)

    }
   }
}


@Composable
fun MessageBubble(message: Messages) {
    val alignment = if (message.user.userId == USERID) Alignment.End else Alignment.Start
    val background = if (message.user.userId == USERID) Color(0xFFFF6921D) else textColor

    val formattedDate = formatDate(message.createdAt)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .background(background, RoundedCornerShape(16.dp))
                .padding(12.dp)

        ) {
            message.text?.let {
                Text(

                    text = it,
                    textAlign = TextAlign.End,
                    color = Color.White
                )

            }
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp , vertical = 8.dp),
            text = formattedDate,
            textAlign = TextAlign.End,
            color = Color(0xFFF909093)
        )
    }
}


fun formatDate(apiDate: String): String {
    val zonedDateTime = ZonedDateTime.parse(apiDate)
    val formatter = DateTimeFormatter.ofPattern("HH:mm • dd MMMM yyyy")
    return zonedDateTime.format(formatter)
}


@Composable
fun ShowTopics(topics: List<String>) {

    if (topics.isNullOrEmpty()) {
        Text(
            text = "No topics available",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(topics.chunked(3)){topic ->
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(topic){topic ->

                        TopicButtons(
                            topicTitle = topic  ,
                        )
                    }

                }
            }
        }
    }
}
@Composable
fun TopicButtons(topicTitle: String) {
    val backgroundColor =  Color(0xFFFF6921D)
    val textColor =  Color.Black
    val borderColor =  Color.Transparent

    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { }
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Text(
            text = topicTitle,
            color = textColor,
            fontWeight = FontWeight.Bold
            )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun MessagingScreenTheme(){
    MessagingScreen()
}*/
