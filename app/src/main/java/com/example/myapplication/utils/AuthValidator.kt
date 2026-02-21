package com.example.myapplication.utils

import com.example.myapplication.presentation.views.auth.states.AuthFormState

sealed class AuthValidator {

    companion object {
        const val EMAIL_REGEX: String = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
        const val PASSWORD_MIN_LENGTH = 8

        fun validateEmail(text: String): Boolean {
            val mask: String = EMAIL_REGEX
            if (text.isEmpty()) return true
            return text.matches(mask.toRegex())
        }

        fun arePasswordsEqual(password: String, repeatedPassword: String): Boolean {
            if (password.isEmpty() && repeatedPassword.isEmpty()) return true
            return password == repeatedPassword
        }

        fun validatePassword(password: String): Boolean {
            if (password.isEmpty()) return true
            return password.length >= PASSWORD_MIN_LENGTH
        }

        fun validateAuthForm(authFormState: AuthFormState): Boolean {
            val isLoginFormValid = authFormState.isEmailValid
                    && authFormState.isPasswordValid
                    && authFormState.email.isNotEmpty()
                    && authFormState.password.isNotEmpty()

            return isLoginFormValid
        }
    }
}