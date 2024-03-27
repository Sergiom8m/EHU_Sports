package com.example.menditrack.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
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
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    var context = LocalContext.current

    var emptyFields = stringResource(R.string.empty_fields)
    var diffPsw = stringResource(R.string.diff_psw)
    var wrongPsw = stringResource(R.string.wrong_psw)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro",
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { FocusDirection.Down }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
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
            label = { Text("Repetir contraseña") },
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
                            navController.navigate(AppScreens.UserScreen.route)
                        } else {
                            Log.d("REG", "El usuario existe")
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse")
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