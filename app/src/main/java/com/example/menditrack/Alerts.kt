package com.example.menditrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.menditrack.data.Language

@Composable
fun SettingsDialog(showSettings: Boolean, appViewModel: AppViewModel, onConfirm: () -> Unit) {
    if (showSettings) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.accept))
            }
            },
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
                                appViewModel.setLanguage(language)
                            },
                            Modifier.fillMaxWidth()
                        ) {
                            Text(text = language.type)
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
