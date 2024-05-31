package com.example.scareme.ui.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.scareme.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.splash),
        contentDescription = null,
        modifier = modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop

        )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenTheme(){
    SplashScreen(modifier = Modifier)
}