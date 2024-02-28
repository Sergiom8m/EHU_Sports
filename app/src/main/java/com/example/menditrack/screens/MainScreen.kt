package com.example.menditrack.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.InfoDialog
import com.example.menditrack.R
import com.example.menditrack.SettingsDialog
import com.example.menditrack.data.Design
import com.example.menditrack.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    modifier: Modifier,
    languageChange: (String) -> Unit
){

    val language = appViewModel.actual_language
    languageChange(language.code)

    val navController = rememberNavController()

    val showAddButton = appViewModel.showAddButton
    val showSettingButton = appViewModel.showSettingButton
    val enableButtons = appViewModel.enableNavigationButtons

    Scaffold (
        topBar = {
            if (showSettingButton) {
                TopBar(navController, appViewModel, modifier, LocalContext.current)
            }
         },
        bottomBar = { BottomBar(navController, appViewModel, modifier, enableButtons) },
        floatingActionButton = {

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            if (navBackStackEntry?.destination?.route != AppScreens.Stats.route && showAddButton) {
                Button(navController, appViewModel, modifier)
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreens.Stats.route
        ){
            composable(AppScreens.Walking.route) { Walking(appViewModel, navController) }
            composable(AppScreens.Running.route) { Running(appViewModel, navController) }
            composable(AppScreens.Cycling.route) {Cycling(appViewModel, navController)}
            composable(AppScreens.Stats.route) { Stats(appViewModel, navController) }
            composable(AppScreens.Add.route)  {AddActivity(appViewModel, navController) }
        }
    }
}



@Composable
fun Button(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {
    FloatingActionButton(
        onClick = {
            navController.navigate(AppScreens.Add.route)
            appViewModel.showAddButton = false
            appViewModel.showSettingButton = false
          },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape
    ) {
        Icon(Icons.Filled.Add, stringResource(id = R.string.add))
    }
}


@Composable
fun BottomBar(navController: NavController, appViewModel: AppViewModel, modifier: Modifier, enableButtons: Boolean) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        val items = listOf(
            Design(AppScreens.Walking, stringResource(id = R.string.walking ), Icons.Filled.Place),
            Design(AppScreens.Running, stringResource(id = R.string.running), Icons.Filled.Place),
            Design(AppScreens.Cycling, stringResource(id = R.string.cycling), Icons.Filled.Place),
            Design(AppScreens.Stats, stringResource(id = R.string.stats), Icons.Filled.Place)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(screen.icon, contentDescription = null, tint = Color.White) },
                label = {
                    Text(
                        screen.name,
                        color = Color.White,
                        overflow = TextOverflow.Visible
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.screen.route } == true,
                onClick = {
                    if (enableButtons) {
                        navController.navigate(screen.screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                enabled = enableButtons
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, appViewModel: AppViewModel ,modifier: Modifier, context: Context) {
    
    var showInfo by rememberSaveable { mutableStateOf(false) }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text( text = stringResource(id = R.string.app_name))
                },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color(0xFFFFFFFF),
        ),

        navigationIcon = {
            IconButton(onClick = { (context as? Activity)?.finish() }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = stringResource(id = R.string.go_back),
                    tint = Color(0xFFFFFFFF)
                )
            }
        },

        actions = {
            IconButton(onClick = { showInfo = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(id = R.string.info),
                    tint = Color(0xFFFFFFFF)
                )
            }
            IconButton(onClick = { showSettings = true}) {
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = stringResource(id = R.string.settings),
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    )
    InfoDialog(showInfo) { showInfo = false }
    SettingsDialog(showSettings, appViewModel) { showSettings = false }
    
}

