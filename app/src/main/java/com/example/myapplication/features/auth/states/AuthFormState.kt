package com.example.myapplication.features.auth.states

import com.example.myapplication.data.dto.UserCredentials
import com.example.myapplication.data.models.enums.FetchState

data class AuthFormState(
    override var email: String = "",
    override var password: String = "",
    val state: FetchState = FetchState.INITIAL,

    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isFormValid: Boolean = isEmailValid && isPasswordValid,
) : UserCredentials