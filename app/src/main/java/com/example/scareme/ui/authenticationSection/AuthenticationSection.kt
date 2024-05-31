package com.example.scareme.ui.authenticationSection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scareme.R


@Composable
fun AuthenticationSection(){
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.scare_me),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 268.dp, height = 122.dp)
            )
        }
            Image(
                painter = painterResource(id = R.drawable.ellipse_13) ,
                contentDescription = null,
                        modifier = Modifier
                            .size(width = 100.dp, height = 100.dp)
                            .offset(x = 53.dp, y = 213.dp)
            )
        Image(
            painter = painterResource(id = R.drawable.ellipse_15) ,
            contentDescription = null,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .offset(x = 216.dp, y = 204.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ellipse_14) ,
            contentDescription = null,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .offset(x = 30.dp, y = 340.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ellipse_16) ,
            contentDescription = null,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .offset(x = 250.dp, y = 360.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ellipse_17) ,
            contentDescription = null,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .offset(x = 62.dp, y = 480.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ellipse_18) ,
            contentDescription = null,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .offset(x = 220.dp, y = 500.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Button(
                onClick = {  },
                modifier = Modifier
                    .offset(y = 660.dp)
                    .size(width = 328.dp, height = 56.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors( Color(0xFFFFA500)),
                shape = RoundedCornerShape(12.dp)

            ) {
                Text(
                    text = "Sign up",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                    )
            }
            Spacer(modifier = Modifier.height(686.dp))
            Text(
                text = "Already have an account?",
                color = Color(0xFFB14623)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .clickable {

                    },
                text = "Sign in.",
                color = Color(0xFFF6921D)
            )


        }

    }

}

@Preview(showBackground = true)
@Composable
fun AuthenticationScreenTheme() {
    AuthenticationSection()
}