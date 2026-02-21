package com.example.myapplication.data

interface UserCredentials {
    var email: String
    var password: String
}

data class UserCredentialsDto(
    override var email: String,
    override var password: String
) : UserCredentials