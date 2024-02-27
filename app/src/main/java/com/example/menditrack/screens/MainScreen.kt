package com.example.menditrack.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.R
import com.example.menditrack.data.Design
import com.example.menditrack.data.Language
import com.example.menditrack.navigation.AppScreens
import java.util.Locale

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

    Scaffold (
        topBar = { TopBar(appViewModel, modifier, LocalContext.current)},
        bottomBar = { BottomBar(navController, appViewModel, modifier) },
        floatingActionButton = { FloatingButton(navController, appViewModel, modifier) }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreens.Home.route
        ){
            composable(AppScreens.Feed.route) { Feed(innerPadding, appViewModel, navController)}
            composable(AppScreens.Home.route) { Home(innerPadding, appViewModel, navController)}
            composable(AppScreens.Profile.route) {Profile(innerPadding, appViewModel, navController)}
        }
    }
}


@Composable
fun FloatingButton(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {

}

@Composable
fun BottomBar(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        val items = listOf(
            Design(AppScreens.Feed, "Feed", Icons.Filled.List),
            Design(AppScreens.Home, "Home", Icons.Filled.Home),
            Design(AppScreens.Profile, "Profile", Icons.Filled.AccountCircle)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(screen.icon, contentDescription = null, tint = Color.White) },
                label = { Text(screen.name, color = Color.White) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.screen.route } == true,
                onClick = {
                    navController.navigate(screen.screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(appViewModel: AppViewModel, modifier: Modifier, context: Context) {
    
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
                    imageVector = Icons.Filled.KeyboardArrowLeft,
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

@Composable
fun SettingsDialog(showSettings: Boolean, appViewModel: AppViewModel, onConfirm: () -> Unit) {
    if (showSettings) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.accept))
            }},
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


@Composable
fun BodyContent(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {


}
