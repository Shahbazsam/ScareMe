package com.example.scareme.profile.presentation

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scareme.R
import com.example.scareme.profile.data.model.Topics
import com.example.scareme.profile.data.model.UserInformation


@Composable
fun ShowProfileScreen(
    viewModel: ShowProfileViewModel,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){


    //val viewModel : ShowProfileViewModel = viewModel(factory = ShowProfileViewModel.Factory)
    val uiState by  viewModel.showProfileUiState.collectAsState()

    val showProfileUiState = uiState

    when(showProfileUiState){
        is ShowProfileUiState.Loading -> LoadingProfileScreen(modifier = modifier.fillMaxSize())
        is ShowProfileUiState.Success -> SuccessScreen(
           userInformation =  showProfileUiState.userInformation,  modifier = modifier.fillMaxWidth()
        )
        is ShowProfileUiState.Error -> ErrorProfileScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingProfileScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(80.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = null
    )
}

@Composable
fun ErrorProfileScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun SuccessScreen(userInformation: UserInformation, modifier: Modifier = Modifier){

    val user = userInformation
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = 400.dp, height = 370.dp),
                painter = painterResource(id = R.drawable.background),
                contentDescription =null
            )
            user?.let {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    model = ImageRequest.Builder(context = LocalContext.current )
                        .data(it.avatar)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
            user?.let {
                Text(
                    text = it.name ?: "" ,
                    color = Color.White,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(y = 140.dp)
                )
            }
        }

        Spacer(modifier = Modifier.padding(12.dp))

        val fruits = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Honeydew", "Indian Fig", "Jackfruit")

        user?.let{
                        ShowTopics(it.topics)
            }
        Spacer(modifier = Modifier.padding(12.dp))
        user?.let {
            Box (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(x = 23.dp, y = 21.dp)
                    .size(width = 354.dp, height = 101.dp)

            ){
                Text(
                    text = it.aboutMyself ?: "No Information Available",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()

                )

            }
        }




    }

}



@Composable
fun ShowTopics(topics: List<Topics>?) {

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
                .height(160.dp)
        ) {
            items(topics.chunked(4)){topic ->
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(topic){topic ->

                            TopicButtons(
                                topicTitle = topic.title  ,
                            )
                    }

                }
            }
        }
    }
}
@Composable
fun TopicButtons(topicTitle: String) {
    val backgroundColor =  Color(0xFFFFA500)
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
        Text(text = topicTitle, color = textColor)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SuccessScreen(
        userInformation =
            UserInformation(
                userId = "hhgghhhg",
                name = "John Doe",
                avatar = "http://itindr.mcenter.pro:8092/static/avatar_313f445d-7e23-4c97-97b3-b1963244a633.png",
                aboutMyself = "Hello, I am John Doe.",
                topics = listOf(
                    Topics(id = "1", title = "Kotlin"),
                    Topics(id = "2", title = "Compose"),
                    Topics(id = "3", title = "Android")
                )
            )
        )
}
