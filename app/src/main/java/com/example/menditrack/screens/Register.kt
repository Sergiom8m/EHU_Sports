package com.example.menditrack.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.menditrack.R
import com.example.menditrack.data.User
import com.example.menditrack.navigation.AppScreens
import com.example.menditrack.viewModel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Register(
    appViewModel: AppViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var password2 by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    val emptyFields = stringResource(R.string.empty_fields)
    val diffPsw = stringResource(R.string.diff_psw)
    val wrongPsw = stringResource(R.string.wrong_psw)
    val userExist = stringResource(R.string.user_exist)


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.register),
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = stringResource(R.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { FocusDirection.Down }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(R.string.password)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { FocusDirection.Down }),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password2,
            onValueChange = { password2 = it },
            label = { Text(text = stringResource(R.string.rep_psw)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { FocusDirection.Down }),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    if (checkRegistrationFields(username, password, password2, context, emptyFields, diffPsw, wrongPsw)) {
                        if (appViewModel.addUser(username, password2)) {
                            appViewModel.actualUser = mutableStateOf(User(username, password))
                            username = ""
                            password = ""
                            password2 = ""
                            navController.navigate(AppScreens.UserScreen.route)
                        } else {
                            Toast.makeText(context, userExist,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.register))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.account),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = { navController.navigate(AppScreens.Login.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.login))
        }
    }
}


fun checkRegistrationFields(username: String, password: String, password2: String, context: Context, emptyFields: String, diffPsw: String, wrongPsw: String): Boolean {
    if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
        Toast.makeText(context, emptyFields,Toast.LENGTH_SHORT).show()
        return false
    }

    if (password != password2) {
        Toast.makeText(context, diffPsw,Toast.LENGTH_SHORT).show()
        return false
    }

    val minPasswordLength = 6
    if (password.length < minPasswordLength) {
        Toast.makeText(context, wrongPsw,Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}