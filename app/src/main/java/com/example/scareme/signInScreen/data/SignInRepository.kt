package com.example.scareme.signInScreen.data

import com.example.scareme.signInScreen.data.model.Token
import com.example.scareme.signInScreen.data.model.UserData
import com.example.scareme.signInScreen.network.SignInApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

interface SignInRepository {
    suspend fun getSignedIn(userData: UserData) : Token
}

class NetworkSignInRepository(
    private val signInApiService: SignInApiService
) : SignInRepository {

    override suspend fun getSignedIn(userData: UserData): Token {
        return withContext(Dispatchers.IO) {
            try {
                signInApiService.register(userData)
            } catch (e: HttpException) {

                when (e.code()) {
                    400 -> {

                        val errorJson = e.response()?.errorBody()?.string()

                        throw Exception("Invalid input. Please check your details.")
                    }
                    404 -> throw Exception("Invalid email or Password")
                    else -> throw Exception("Sign In failed with HTTP error: ${e.code()}")
                }
            } catch (e: IOException) {
                throw Exception("Network error occurred: ${e.message}")
            } catch (e: Exception) {
                throw Exception("An unexpected error occurred: ${e.message}")
            }
        }
    }
}