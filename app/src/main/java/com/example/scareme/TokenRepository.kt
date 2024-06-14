package com.example.scareme

class TokenRepository {
    private var myToken : String = ""

    fun getToken() : String{
        return myToken
    }
    fun setToken(str : String){
        myToken = str
    }
}