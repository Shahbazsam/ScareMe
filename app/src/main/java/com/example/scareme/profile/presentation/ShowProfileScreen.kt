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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){


    val viewModel : ShowProfileViewModel = viewModel(factory = ShowProfileViewModel.Factory)
    val uiState by  viewModel.showProfileUiState.collectAsState()

    val showProfileUiState = uiState

    when(showProfileUiState){
        is ShowProfileUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is ShowProfileUiState.Success -> SuccessScreen(
           userInformation =  showProfileUiState.userInformation,  modifier = modifier.fillMaxWidth()
        )
        is ShowProfileUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }


}

@Composable
fun SuccessScreen(userInformation: List<UserInformation>, modifier: Modifier = Modifier){

    val user = userInformation.firstOrNull()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {


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
                        .fillMaxWidth()
                        .size(width = 400.dp, height = 370.dp),
                    model = ImageRequest.Builder(context = LocalContext.current )
                        .data(it.avatar)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
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
                    .size(width = 314.dp, height = 61.dp)

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
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(60.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp,
            content = {
                items(topics) { topic ->
                    TopicButtons(
                        topicTitle = topic.title,
                        onClick = { /* Handle onClick */ }
                    )
                }
            }
        )
    }
}


@Composable
fun TopicButtons(topicTitle: String, onClick: () -> Unit) {
    val backgroundColor =  Color(0xFFFFA500)
    val textColor =  Color.Black
    val borderColor = Color.Transparent

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





@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SuccessScreen(
        userInformation = listOf(
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
    )
}
