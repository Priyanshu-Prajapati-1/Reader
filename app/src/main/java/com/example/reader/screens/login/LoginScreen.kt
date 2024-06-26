package com.example.reader.screens.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reader.R
import com.example.reader.components.EmailInput
import com.example.reader.components.ReaderLogo
import com.example.reader.navigation.ReaderScreens
import com.example.reader.utils.checkEmailAndPassword

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    Surface(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReaderLogo(modifier = Modifier.padding(vertical = 20.dp))

            Text(
                text = if (showLoginForm.value) "Login" else "Sign Up",
                modifier = Modifier.padding(bottom = 15.dp),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            UserForm(
                loading = false,
                isCreateAccount = false,
                viewModel = viewModel
            ) { email, password ->
                Log.d("FBR", "signInWithEmailAndPassword: ye $email and $password")

                if (showLoginForm.value) {
                    // TODO: FireBase Login
                    viewModel.signInWithEmailAndPassword(email, password) {
                        navController.navigate(ReaderScreens.HomeScreen.name) {
                            popUpTo(0)
                        }
                    }
                } else {
                    // TODO: create FireBase account
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(ReaderScreens.HomeScreen.name) {
                            popUpTo(0)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm.value) "Sign Up" else "Login"
                Text(text = if (showLoginForm.value) "New User? " else "Have Account: ")
                Text(
                    text = text,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@SuppressLint("RememberReturnType", "UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    viewModel: LoginScreenViewModel,
    onDone: (String, String) -> Unit = { email, pass -> },
) {

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }

    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value, password.value) {
//        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
        checkEmailAndPassword(email.value, password.value)
    }

    val modifier = Modifier
        .height(300.dp)
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())                                       // for scrollable


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!valid) {
            Text(
                text = stringResource(id = R.string.email_password),
                modifier = Modifier.padding(horizontal = 30.dp),
                color = MaterialTheme.colorScheme.error,
                maxLines = 4,
                style = TextStyle(
                    fontSize = 12.sp
                )
            )
        }

        EmailInput(
            modifier = Modifier.padding(bottom = 10.dp, start = 20.dp, end = 20.dp),
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions { passwordFocusRequest.requestFocus() }
        )

        PasswordInput(
            modifier = Modifier
                .focusRequester(passwordFocusRequest)
                .padding(bottom = 10.dp, start = 20.dp, end = 20.dp),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
//                keyboardController?.hide()
            }
        )

        if (viewModel.onWrongEmailPassword.value) {
            Text(
                text = "Invalid email and password",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            )
        }

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid,
            viewModel = viewModel
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    viewModel: LoginScreenViewModel,
    onClick: () -> Unit
) {
    val isCircularLoading = rememberSaveable {
        mutableStateOf(false)
    }

    Button(
        onClick = {
            isCircularLoading.value = !isCircularLoading.value
            onClick()
        },
        modifier = Modifier
            .padding(20.dp)
//            .height(50.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer
        ),
        enabled = !loading && validInputs,
        shape = CircleShape,

        ) {
        if (isCircularLoading.value) {
            if (viewModel.onWrongEmailPassword.value) {
                isCircularLoading.value = false
            }
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(
                text = textId,
                modifier = Modifier.padding(5.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {

    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = {
            Text(
                text = labelId,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            PasswordVisibility(passwordVisibility = passwordVisibility)
        },
        shape = RoundedCornerShape(12.dp),
        keyboardActions = onAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value

    IconButton(
        onClick = { passwordVisibility.value = !visible },
        modifier = Modifier.padding(end = 5.dp),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Icons.Default.Close
    }
}



