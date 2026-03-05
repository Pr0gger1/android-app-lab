package com.example.myapplication.features.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.core.shared.CircleLoader
import com.example.myapplication.data.models.enums.FetchState
import com.example.myapplication.features.auth.AuthViewModel
import com.example.myapplication.features.auth.states.AuthFormState
import com.example.myapplication.utils.AuthValidator

@Composable
fun AuthView(authViewModel: AuthViewModel, padding: PaddingValues) {
    val authFormState by authViewModel.authFormState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authFormState.state) {
        when (authFormState.state) {
            FetchState.ERROR -> {
                Toast.makeText(
                    context,
                    "Неправильный логин или пароль",
                    Toast.LENGTH_SHORT
                ).show()
            }

            FetchState.SUCCESS -> {
                authViewModel.redirect(context)
                authViewModel.setFormState(FetchState.INITIAL)
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.padding(padding)) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.bird_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = authFormState.email,
                    singleLine = true,
                    onValueChange = authViewModel::setEmail,
                    label = { Text(stringResource(id = R.string.email_label)) },
                    isError = !authFormState.isEmailValid || authFormState.state == FetchState.ERROR,
                    supportingText = {
                        if (!authFormState.isEmailValid)
                            Text(
                                text = stringResource(id = R.string.invalid_email),
                                color = MaterialTheme.colorScheme.error
                            )
                    },
                    placeholder = { Text(stringResource(R.string.email_placeholder)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = authFormState.password,
                    singleLine = true,
                    onValueChange = authViewModel::setPassword,
                    label = { Text(stringResource(id = R.string.password_label)) },
                    isError = !authFormState.isPasswordValid || authFormState.state == FetchState.ERROR,
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text(stringResource(R.string.password_placeholder)) },
                    supportingText = { ErrorPasswordFieldMessage(authFormState) }
                )

                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(),
                    onClick = authViewModel::signIn,
                    enabled = AuthValidator.validateAuthForm(authFormState)
                ) {
                    if (authFormState.state == FetchState.LOADING)
                        CircleLoader(modifier = Modifier.size(24.dp))
                    else Text(
                        text = stringResource(id = R.string.auth_button_login_name),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorPasswordFieldMessage(authFormState: AuthFormState) {
    Column {
        if (!authFormState.isPasswordValid)
            Text(
                text = stringResource(id = R.string.invalid_password),
                color = MaterialTheme.colorScheme.error
            )
    }
}