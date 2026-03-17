package com.example.myapplication.features.auth

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.datastore.UserDataStore
import com.example.myapplication.data.datastore.UserPreferences
import com.example.myapplication.data.models.enums.FetchState
import com.example.myapplication.features.auth.states.AuthFormState
import com.example.myapplication.features.home.HomeActivity
import com.example.myapplication.utils.AuthValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    appDatabase: AppDatabase
) : ViewModel() {
    private val _authFormState = MutableStateFlow(AuthFormState())
    val authFormState = _authFormState.asStateFlow()
    val userDao = appDatabase.getUserDao()

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

    fun setFormState(state: FetchState) {
        _authFormState.update {
            it.copy(state = state)
        }
    }

    fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            setFormState(FetchState.LOADING)

            val typedEmail = authFormState.value.email
            val typedPassword = authFormState.value.password

            val user = userDao.getByEmail(typedEmail)

            if (user == null || user.password != typedPassword) {
                setFormState(FetchState.ERROR)
                return@launch
            }

            userDataStore.updateCreds(UserPreferences(email = user.email))
            setFormState(FetchState.SUCCESS)
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