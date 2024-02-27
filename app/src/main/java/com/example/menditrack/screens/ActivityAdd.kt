package com.example.menditrack.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.menditrack.AppViewModel

@Composable
fun AddActivity(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    var routeName by remember { mutableStateOf("") }
    var routeDistance by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = routeName,
            onValueChange = { routeName = it },
            label = { Text("Nombre de la ruta") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = routeDistance,
            onValueChange = { routeDistance = it },
            label = { Text("Distancia de la ruta (en km)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                appViewModel.setAddButtonVisibility(true)
                navController.navigateUp()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Añadir ruta")
        }
    }

    DisposableEffect(Unit) {
        // Deshabilitar los botones de la barra de navegación
        appViewModel.enableNavigationButtons = false
        onDispose {
            // Volver a habilitar los botones de la barra de navegación al salir de la pantalla de añadir
            appViewModel.enableNavigationButtons = true
            appViewModel.setAddButtonVisibility(true)
            Log.d("FUNCIONA", "FUNCIONAAAAA")
        }
    }

}
