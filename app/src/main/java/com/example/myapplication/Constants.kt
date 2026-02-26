package com.example.myapplication

import com.example.myapplication.data.dto.UserCredentialsDto

object Constants {
    val USER_CREDENTIALS = listOf(
        UserCredentialsDto("ivan@mail.ru", "12345678"),
        UserCredentialsDto("elena@mail.ru", "elena.1234"),
        UserCredentialsDto("nikolay@mail.ru", "nikolay.1234"),
        UserCredentialsDto("vladimir@mail.ru", "vladimir.1234"),
        UserCredentialsDto("yuriy@mail.ru", "yuriy.1234")
    )

    const val BASE_API_URL = "'https://fakestoreapi.com"
}