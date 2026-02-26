package com.example.myapplication.features.auth.states

import com.example.myapplication.data.dto.UserCredentials

enum class FormState {
    INITIAL,
    LOADING,
    SUCCESS,
    ERROR
}

data class AuthFormState(
    override var email: String = "",
    override var password: String = "",
    val state: FormState = FormState.INITIAL,

    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isFormValid: Boolean = isEmailValid && isPasswordValid,
) : UserCredentials