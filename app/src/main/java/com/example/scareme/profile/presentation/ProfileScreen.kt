package com.example.scareme.profile.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scareme.R
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var profileImage by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val viewModel : ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
    val state = viewModel.state

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val bitmap = getBitmapFromUri(context, it)
                profileImage = bitmap
            }
        }
    )

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
                .offset(x = 50.dp, y = 57.dp),
            text = "Why are you Scary?",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        // Avatar Image
        if (profileImage != null) {
            Image(
                bitmap = profileImage!!.asImageBitmap(),
                contentDescription = "Profile Image",
                    modifier = Modifier
                        .offset(x = 0.dp, y = 40.dp)
                        .padding(8.dp)
                        .size(200.dp, 200.dp)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") },
            )
        } else {
            Image(
                modifier = Modifier
                    .offset(x = 0.dp, y = 40.dp)
                    .padding(8.dp)
                    .size(200.dp, 200.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null
            )
        }

        // Name TextField
        TextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(ProfileEvent.NameChanged(it)) },
            modifier = Modifier
                .size(width = 370.dp, height = 72.dp)
                .offset(y = 40.dp)
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
            onValueChange = { /* Handle About Change */ },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 370.dp, height = 152.dp)
                .offset(y = 40.dp)
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
                .padding(18.dp)
                .size(width = 328.dp, height = 42.dp)
                .offset(x = 90.dp, y = 42.dp),
            text = "Party Topics",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(12.dp))
        when (profileUiState){
            is ProfileUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is ProfileUiState.Success -> TopicScreen(
                profileUiState.topics , contentPadding = contentPadding , modifier = Modifier.fillMaxWidth()
            )
            is ProfileUiState.Error -> ErrorScreen(retryAction  , modifier = Modifier.fillMaxSize())

        }

        // Save Button
        Button(
            onClick = { viewModel.onEvent(ProfileEvent.Submit) },
            modifier = Modifier
                .offset(y = 75.dp)
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

@Composable
fun TopicScreen(
    topics : List<Topics> ,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){

    LazyVerticalStaggeredGrid(

        columns = StaggeredGridCells.Adaptive(60.dp),
        modifier = Modifier

            .fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
        content = {
            items(topics) { topic ->
                var isSelected by remember { mutableStateOf(false) }
                TopicButton(
                    topicTitle = topic.title  ,
                    isSelected = isSelected,
                    onClick = { isSelected = !isSelected }
                )
            }
        }
    )

}

@Composable
fun TopicButton(topicTitle: String,isSelected: Boolean, onClick: () -> Unit) {
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
        modifier = modifier.size(200.dp),
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

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileScreen()
} */

private fun getBitmapFromUri(context: android.content.Context, uri: Uri): Bitmap? {
    return try {
        android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } catch (e: Exception) {
        null
    }
}