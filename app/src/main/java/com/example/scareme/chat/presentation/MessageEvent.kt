package com.example.scareme.chat.presentation

sealed class MessageEvent{
    data class MessageChanged(val message :String) : MessageEvent()
    object submit : MessageEvent()
}