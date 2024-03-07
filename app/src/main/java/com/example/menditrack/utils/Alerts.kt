package com.example.menditrack.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.menditrack.R
import com.example.menditrack.data.Language

// Dialog to select the language (user preference)
@Composable
fun SettingsDialog(showSettings: Boolean, onLanguageChange: (Language)-> Unit, onConfirm: () -> Unit) {
    if (showSettings) {
        AlertDialog(
            onDismissRequest = { onConfirm() },
            confirmButton = { /*TODO*/ },
            title = { Text(text = stringResource(id = R.string.settings)) },
            text = {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    for (language in Language.entries){
                        Button(
                            onClick = {
                                onConfirm()
                                onLanguageChange(Language.getFromCode(language.code))
                            },
                            Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = language.type,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        )
    }
}

// Dialog to show the information of the app
@Composable
fun InfoDialog(showInfo: Boolean, onConfirm: () -> Unit) {
    if (showInfo) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.accept))
            }},
            title = { Text(text = stringResource(id = R.string.app_name)) },
            text = { Text(text = stringResource(id = R.string.app_desc)) }

        )
    }
}

// Dialog to select the theme (user preference)
@Composable
fun ShowThemes(showThemes: Boolean, onThemeChange: (Int) -> Unit, onConfirm: () -> Unit) {
    if (showThemes) {
        AlertDialog(
            onDismissRequest = { onConfirm() },
            confirmButton = { /* TODO */ },
            title = { Text(text = stringResource(id = R.string.themeAlert)) },
            containerColor = Color.White,
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    val themes = listOf(
                        Pair(R.drawable.primary_palette, "Theme 1"),
                        Pair(R.drawable.secondary_palette, "Theme 2"),
                        Pair(R.drawable.tertiary_palette, "Theme 3")
                    )

                    // Crear un botón para cada tema
                    themes.forEachIndexed { index, (image, name) ->

                        Image(
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(55.dp)
                                .clickable(onClick = {
                                    onConfirm()
                                    onThemeChange(index)
                                }))
                    }
                }
            }
        )
    }
}

