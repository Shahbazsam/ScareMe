package com.example.scareme.profile.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scareme.MockDataRepo.MockProfileRepository
import com.example.scareme.R
import com.example.scareme.ScareMeApplication
import com.example.scareme.TinderNav
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController : NavController,
    retryAction: () -> Unit,
    viewModel: ProfileViewModel,

    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    val uiState by viewModel.profileUiState.collectAsState()

    val profileUiState = uiState
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.onEvent(ProfileEvent.ImageSelected(it))
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect{event ->
            when(event){
                is ProfileViewModel.ValidationEvent.Success ->{
                    Toast.makeText(
                        context,
                        "Profile Updated Successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(TinderNav)
                }
            }
        }
    }
    val state = viewModel.state
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            modifier = Modifier
                .size(width = 328.dp, height = 42.dp)
                .offset(x = 50.dp, y = 27.dp),
            text = "Why are you Scary?",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            Modifier
                .offset(x = 0.dp, y = 5.dp)
                .padding(8.dp)
                .size(190.dp, 190.dp)
                .border(BorderStroke(4.dp, Color(0xFFF6921D)), CircleShape)
                .clip(CircleShape)
                .clickable { launcher.launch("image/*") }
        ) {
            // Display the selected image from the viewModel
            var imageUri = viewModel.selectedImageUri
            if (imageUri != null) {
                val bitmap = getBitmapFromUri(context, imageUri)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Avatar photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {

                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Avatar photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

          /*  Image(
                modifier = Modifier
                    .offset(x = 0.dp, y = 5.dp)
                    .padding(8.dp)
                    .size(200.dp, 200.dp)
                    .clip(CircleShape)
                    .clickable { },
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null
            )*/

        // Name TextField
        TextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(ProfileEvent.NameChanged(it)) },
            modifier = Modifier
                .size(width = 370.dp, height = 72.dp)
                .offset(y = 0.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Name")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
    state.aboutMyself?.let{
        // About TextField
        TextField(
            value = state.aboutMyself,
            onValueChange = { viewModel.onEvent(ProfileEvent.aboutMyselfChanged(it)) },
            modifier = Modifier
                .padding(6.dp)
                .size(width = 370.dp, height = 152.dp)
                .offset(y = 0.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "About")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
    }

        Text(
            modifier = Modifier
                .padding(8.dp)
                .size(width = 328.dp, height = 42.dp)
                .offset(x = 90.dp, y = 0.dp),
            text = "Party Topics",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Box (modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)){
            when (profileUiState){
                is ProfileUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is ProfileUiState.Success -> TopicScreen(
                    profileUiState.topics ,viewModel, contentPadding = contentPadding , modifier = Modifier.fillMaxWidth()
                )
                is ProfileUiState.Error -> ErrorScreen(retryAction  , modifier = Modifier.fillMaxSize())

            }
            //Spacer(modifier = Modifier.height(1.dp))
            // Save Button
            Button(
                onClick = { viewModel.onEvent(ProfileEvent.Submit) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(width = 370.dp, height = 64.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(Color(0xFFFFA500)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Save",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

        }

    }
}



@Composable
fun TopicScreen(
    topics : List<Topics> ,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        items(topics.chunked(3)){topic ->
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(topic){topic ->
                    val isSelected = viewModel.state.topics?.contains(topic.id)
                    if (isSelected != null) {
                        TopicButton(
                            topicTitle = topic.title  ,
                            topicId = topic.id,
                            isSelected = isSelected,
                            onClick = { viewModel.onEvent(ProfileEvent.TopicsSelected(topic.id)) }
                        )
                    }

                }

            }
        }
    }
}

@Composable
fun TopicButton(topicTitle: String,topicId: String,isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFFFA500) else Color.Black
    val textColor = if (isSelected) Color.Black else Color(0xFFFFA500)
    val borderColor = if (isSelected) Color.Transparent else Color(0xFFFFA500)
    
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Text(text = topicTitle, color = textColor)
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(80.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = null
    )
}
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        BitmapFactory.decodeStream(inputStream)
    }
}

/*@Preview(showBackground = true)
@Composable
fun DefaultProfilePreview() {
    val mockRepository = MockProfileRepository()
    val viewModel = ProfileViewModel(
        profileRepository = mockRepository,
        context =
    )
    ProfileScreen(
        navController = rememberNavController(),
        retryAction = {  },
        viewModel = viewModel
    )
}*/
