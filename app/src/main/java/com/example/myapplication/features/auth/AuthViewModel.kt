package com.example.myapplication.features.auth

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Constants
import com.example.myapplication.data.datastore.UserDataStore
import com.example.myapplication.data.datastore.UserPreferences
import com.example.myapplication.data.dto.UserCredentialsDto
import com.example.myapplication.features.auth.states.AuthFormState
import com.example.myapplication.features.auth.states.FormState
import com.example.myapplication.features.home.HomeActivity
import com.example.myapplication.utils.AuthValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : ViewModel() {
    private val _authFormState = MutableStateFlow(AuthFormState())
    val authFormState = _authFormState.asStateFlow()

    fun setEmail(value: String) = _authFormState.update {
        val isEmailValid = AuthValidator.validateEmail(value)
        it.copy(email = value, isEmailValid = isEmailValid)
    }

    fun setPassword(value: String) = _authFormState.update {
        val isPasswordValid: Boolean = AuthValidator.validatePassword(value)

        it.copy(
            password = value,
            isPasswordValid = isPasswordValid,
        )
    }

    fun setFormState(state: FormState) {
        _authFormState.update {
            it.copy(state = state)
        }
    }

    fun signIn() {
        viewModelScope.launch {
            setFormState(FormState.LOADING)
            delay(2000)

            val typedEmail = authFormState.value.email
            val typedPassword = authFormState.value.password

            val user: UserCredentialsDto? = Constants.USER_CREDENTIALS.find {
                it.email == typedEmail && it.password == typedPassword
            }

            if (user == null) {
                setFormState(FormState.ERROR)
                return@launch
            }

            userDataStore.updateCreds(UserPreferences(email = user.email))
            setFormState(FormState.SUCCESS)
        }
    }

    fun redirect(context: Context) {
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
    }

    suspend fun isAuthorized(): Boolean = userDataStore
        .getCreds()
        .firstOrNull()
        ?.email != null
}