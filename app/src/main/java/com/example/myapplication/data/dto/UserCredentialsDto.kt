package com.example.myapplication.data.dto

interface UserCredentials {
    var email: String
    var password: String
}

data class UserCredentialsDto(
    override var email: String,
    override var password: String
) : UserCredentials