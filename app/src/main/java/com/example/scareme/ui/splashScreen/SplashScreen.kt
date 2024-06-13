package com.example.scareme.ui.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import com.example.scareme.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scareme.AuthenticationSections
import com.example.scareme.ScreenSplash
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController , modifier: Modifier = Modifier){
    LaunchedEffect(Unit) {
        delay(1000L) // 1000 milliseconds delay for 1 second
        navController.navigate(AuthenticationSections) {
            popUpTo(ScreenSplash) { inclusive = true }
        }
    }

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
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}