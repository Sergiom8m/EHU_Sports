package com.example.menditrack.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.menditrack.R
import com.example.menditrack.backgroundServ.AndroidAlarmScheduler
import com.example.menditrack.data.FutureActivity
import com.example.menditrack.utils.addEventOnCalendar
import com.example.menditrack.viewModel.AppViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramActivity(
    appViewModel: AppViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val scheduler = AndroidAlarmScheduler(context)

    val datePickerState = rememberDatePickerState()
    var activityName by rememberSaveable { mutableStateOf("") }

    val errorMessage = stringResource(id = R.string.empty_fields)
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(7.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(id = R.string.add_event),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (datePickerState.selectedDateMillis != null && activityName != "") {
                            addEventOnCalendar(
                                context,
                                activityName,
                                datePickerState.selectedDateMillis!!
                            )
                            val date = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                                ZoneId.systemDefault()
                            ).toLocalDate()
                            scheduler.schedule(
                                FutureActivity(
                                    time = LocalDateTime.of(date.year, date.monthValue, date.dayOfMonth, LocalDateTime.now().hour, LocalDateTime.now().minute+1),
                                    title = context.getString(R.string.notifSched_title, activityName),
                                    body = context.getString(R.string.notifSched_body, activityName)
                                )
                            )
                            navController.popBackStack()
                        }
                        else{
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        OutlinedTextField(
            value = activityName,
            onValueChange = { activityName = it },
            label = { Text(stringResource(id = R.string.route_name)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        DatePicker(
            state = datePickerState,
            modifier = Modifier
                .fillMaxSize()
        )

        // On entering this screen nav bars and add floating button should be hidden
        DisposableEffect(Unit) {
            appViewModel.showNavBars = false
            appViewModel.showAddButton = false

            // On exiting this screen make them visible again
            onDispose {
                appViewModel.showNavBars = true
                appViewModel.showAddButton = true
            }
        }
    }

}