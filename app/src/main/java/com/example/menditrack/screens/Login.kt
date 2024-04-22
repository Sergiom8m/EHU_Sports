package com.example.menditrack.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.menditrack.navigation.AppScreens
import com.example.menditrack.utils.hashPassword
import com.example.menditrack.viewModel.AppViewModel
import com.example.menditrack.widgets.EHUSportsWidget
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Login(
    appViewModel: AppViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
) {
    // Set profile picture to null (will be downloaded after login)
    appViewModel.profilePicture = null

    // Variables to store the introduced data in login form data fields
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    val wrongUsername = stringResource(R.string.wrong_username)
    val wrongPassword = stringResource(R.string.wrong_password)

    // Refresh widget every time the app is opened
    EHUSportsWidget().refresh(context)

    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.POST_NOTIFICATIONS
    )
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions.toList()
    )
    LaunchedEffect(true){
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

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
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(16.dp)
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
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Check user validity get in case of correct get the needed data from remote
                CoroutineScope(Dispatchers.Main).launch {
                    val user = appViewModel.getUser(username)
                    if (user != null) {
                        if (user.username == username && user.password == hashPassword(password)){
                            username = ""
                            password = ""
                            navController.navigate(AppScreens.UserScreen.route)
                            appViewModel.actualUser = mutableStateOf(user)
                            appViewModel.getProfileImage(appViewModel.actualUser.value.username)
                        }
                        else{
                            Toast.makeText(context, wrongPassword, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context, wrongUsername, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.login))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.no_account),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = { navController.navigate(AppScreens.Register.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.register))
        }
    }
}