package com.example.myapplication.presentation.views.auth

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.presentation.activities.AuthenticatedActivity
import com.example.myapplication.presentation.shared.CircleLoader
import com.example.myapplication.presentation.views.auth.states.AuthFormState
import com.example.myapplication.presentation.views.auth.states.FormState
import com.example.myapplication.utils.AuthValidator

@Composable
fun AuthView(authViewModel: AuthViewModel, padding: PaddingValues) {
    val authFormState by authViewModel.authFormState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authFormState.state) {
        when (authFormState.state) {
            FormState.ERROR -> {
                Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
            }

            FormState.SUCCESS -> {
                val intent = Intent(context, AuthenticatedActivity::class.java)
                context.startActivity(intent)
                authViewModel.setFormState(FormState.INITIAL)
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.padding(padding)) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Logo", fontSize = 64.sp)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
                    .padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = authFormState.email,
                    singleLine = true,
                    onValueChange = authViewModel::setEmail,
                    label = { Text(stringResource(id = R.string.email_label)) },
                    isError = !authFormState.isEmailValid,
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
                    isError = !authFormState.isPasswordValid,
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
                    if (authFormState.state == FormState.LOADING)
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