package com.example.scareme.authenticationScreen.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scareme.ProfileInputScreen
import com.example.scareme.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(modifier: Modifier = Modifier , navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val viewModel : AuthenticationViewModel = viewModel(factory = AuthenticationViewModel.Factory)
        val state = viewModel.state
        val context = LocalContext.current
        LaunchedEffect(key1 = context) {
            viewModel.validationEvents.collect{event ->
                when(event){
                    is AuthenticationViewModel.ValidationEvent.Success ->{
                        Toast.makeText(
                            context,
                            "Registration Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(ProfileInputScreen)
                    }
                }
            }
        }
            Text(
                modifier = Modifier
                    .size(width = 343.dp, height = 63.dp)
                    .offset(y = 134.dp),
                text = "Sign Up      ",
                color = Color.White,
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = state.email ,
                onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))
                },
                isError = state.emailError != null,
                modifier = Modifier
                    .size(width = 370.dp, height = 72.dp)
                    .offset(y = 144.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = "E-mail")
                },
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
            if(state.emailError != null){
                Text(
                    modifier = Modifier
                        .offset(y = 144.dp),
                    text = state.emailError,
                    color =  Color(0xFFFFA500)
                )
            }

        TextField(
            value = state.password ,
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))
            },
            isError = state.passwordError != null,
            modifier = Modifier
                .size(width = 370.dp, height = 72.dp)
                .offset(y = 154.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
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
        if(state.passwordError != null){
            Text(
                modifier = Modifier
                    .offset(y = 154.dp),
                text = state.passwordError,
                color =  Color(0xFFFFA500)
            )
        }
        OutlinedTextField(
            value = state.repeatedPassword ,
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it))
            },
            isError = state.repeatedPasswordError != null,
            modifier = Modifier
                .size(width = 370.dp, height = 72.dp)
                .offset(y = 164.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Repeat Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
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
        if(state.repeatedPasswordError != null){
            Text(
                modifier = Modifier
                    .offset(y = 164.dp),
                text = state.repeatedPasswordError,
                color =  Color(0xFFFFA500)
            )
        }
        //Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = {
                    viewModel.onEvent(RegistrationFormEvent.Submit)

            },
            modifier = Modifier
                .offset(y = 430.dp)
                .size(width = 370.dp, height = 64.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors( Color(0xFFFFA500)),
            shape = RoundedCornerShape(16.dp)

        ) {
            Text(
                text = "Sign up",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun AuthenticationScreenPreview(){
    val navController = rememberNavController()
    AuthenticationScreen(navController = navController)
}

