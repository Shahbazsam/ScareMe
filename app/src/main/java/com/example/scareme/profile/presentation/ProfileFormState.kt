package com.example.scareme.profile.presentation

data class ProfileFormState(
    val name : String = "",
    val aboutMyself : String? = "",
    val topics : List<String>? = emptyList()
)
