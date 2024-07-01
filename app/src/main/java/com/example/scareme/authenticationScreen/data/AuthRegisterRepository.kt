package com.example.scareme.authenticationScreen.data

import com.example.scareme.authenticationScreen.data.model.Token
import com.example.scareme.authenticationScreen.data.model.UserData
import com.example.scareme.authenticationScreen.network.AuthApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

import java.io.IOException
interface AuthRegisterRepository {
    suspend fun getRegistered(userData: UserData) : Token
}

class NetworkAuthRegisterRepository(
    private val authApiService: AuthApiService
) : AuthRegisterRepository {

    override suspend fun getRegistered(userData: UserData): Token {
        return withContext(Dispatchers.IO) {
            try {
                authApiService.register(userData)
            } catch (e: HttpException) {

                when (e.code()) {
                    400 -> {
                        val errorJson = e.response()?.errorBody()?.string()
                        throw Exception("Invalid input. Please check your details.")
                    }
                    409 -> throw Exception("An account with this email already exists.")
                    else -> throw Exception("Registration failed with HTTP error: ${e.code()}")
                }
            } catch (e: IOException) {
                throw Exception("Network error occurred: ${e.message}")
            } catch (e: Exception) {
                throw Exception("An unexpected error occurred: ${e.message}")
            }
        }
    }
}