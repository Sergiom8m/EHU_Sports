package com.example.menditrack.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.R
import androidx.compose.material.DropdownMenuItem
import com.example.menditrack.model.SportActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun AddActivity(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
){

    var routeName by rememberSaveable { mutableStateOf("") }
    var routeDistance by rememberSaveable { mutableStateOf("") }
    var startingPoint by rememberSaveable { mutableStateOf("") }
    var grade by rememberSaveable { mutableStateOf("") }

    var selectedSport by rememberSaveable { mutableStateOf("") }
    val sports = listOf(
        stringResource(id = R.string.walking),
        stringResource(id = R.string.running),
        stringResource(id = R.string.cycling)
    )

    var selectedDifficulty by rememberSaveable { mutableStateOf("") }
    val difficulties = listOf(
        stringResource(id = R.string.easy),
        stringResource(id = R.string.moderate),
        stringResource(id = R.string.hard)
    )

    val focusManager = LocalFocusManager.current
    val expandedSport = rememberSaveable { mutableStateOf(false) }
    val expandedDiff= rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val errorMessage = stringResource(id = R.string.wrong_data)
    var title = stringResource(id = R.string.notif_title)
    var content = stringResource(id = R.string.notif_body, selectedSport, routeName)


    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = { navController.navigateUp()
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.add_route),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Divider()

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expandedSport.value = true }),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedSport.isEmpty()) stringResource(id = R.string.select_sport) else selectedSport,
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
            DropdownMenu(
                expanded = expandedSport.value,
                onDismissRequest = { expandedSport.value = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                sports.forEach{sport ->
                    DropdownMenuItem(
                        onClick = {
                            selectedSport = sport
                            expandedSport.value = false
                        }
                    ) {
                        Text(text = sport)
                    }
                }
            }
        }

        OutlinedTextField(
            value = routeName,
            onValueChange = { routeName = it },
            label = { Text(stringResource(id = R.string.route_name)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = routeDistance,
            onValueChange = { routeDistance = it },
            label = { Text(stringResource(id = R.string.route_dist))},
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = startingPoint,
            onValueChange = { startingPoint = it },
            label = { Text(stringResource(id = R.string.start_point)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = grade,
            onValueChange = { grade = it },
            label = { Text(stringResource(id = R.string.grade)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expandedDiff.value = true }),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedDifficulty.isEmpty()) stringResource(id = R.string.select_diff) else selectedDifficulty,
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
            DropdownMenu(
                expanded = expandedDiff.value,
                onDismissRequest = { expandedDiff.value = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                difficulties.forEach { difficultyOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedDifficulty = difficultyOption
                            expandedDiff.value = false
                        }
                    ) {
                        Text(text = difficultyOption)
                    }
                }
            }
        }


        Button(
            onClick = {
                if (isValidInput(
                        routeName,
                        routeDistance,
                        startingPoint,
                        grade,
                        selectedDifficulty,
                        selectedSport)
                    ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        appViewModel.addActivity(SportActivity(routeName, routeDistance.toDouble(), startingPoint, grade.toDouble(), selectedDifficulty, selectedSport))
                    }
                    appViewModel.showAddButton = true
                    navController.navigateUp()
                    appViewModel.sendAddNotification(
                        context,
                        title,
                        content
                    )
                } else {
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.add))
        }
    }

    DisposableEffect(Unit) {
        appViewModel.enableNavigationButtons = false
        appViewModel.showAddButton = false
        appViewModel.showNavBars = false
        onDispose {
            appViewModel.enableNavigationButtons = true
            appViewModel.showNavBars = true
            appViewModel.showAddButton = true
        }
    }
}


private fun isValidInput(
    name: String,
    distance: String,
    point: String,
    grade: String,
    diff: String,
    sport: String): Boolean
{
    val validName = name.isNotBlank()
    val validDistance = distance.toDoubleOrNull() != null && distance.toDouble() > 0
    val validPoint = point.isNotBlank()
    val validGrade = grade.toDoubleOrNull() != null && grade.toDouble() > 0
    val validDiff = diff.isNotBlank()
    val validSport = sport.isNotBlank()
    return validName && validDistance && validPoint && validGrade && validDiff && validSport
}