package com.example.menditrack.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.NavigationRail
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.InfoDialog
import com.example.menditrack.PreferencesViewModel
import com.example.menditrack.R
import com.example.menditrack.SettingsDialog
import com.example.menditrack.ShowThemes
import com.example.menditrack.data.Design
import com.example.menditrack.data.Language
import com.example.menditrack.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    prefViewModel: PreferencesViewModel,
    modifier: Modifier,
){
    val context = LocalContext.current
    val onLanguageChange:(Language)-> Unit = {
        prefViewModel.changeLang(it, context)
    }

    val onThemeChange:(Int)-> Unit = {
        prefViewModel.changeTheme(it)
    }

    val navController = rememberNavController()

    val showAddButton = appViewModel.showAddButton
    val showSettingButton = appViewModel.showNavBars
    val enableButtons = appViewModel.enableNavigationButtons


    val configuration = LocalConfiguration.current
    val isVertical = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isVertical) {

        Scaffold(
            topBar = {
                if (showSettingButton) {
                    TopBar(context, onLanguageChange, onThemeChange, modifier)
                }
            },
            bottomBar = {
                if (showSettingButton) {
                    BottomBar(navController, appViewModel, modifier, enableButtons)
                }
            },
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
            ) {
                composable(
                    AppScreens.Walking.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                ) { AcitivtyList(appViewModel, navController, "Walking") }

                composable(
                    AppScreens.Running.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { AcitivtyList(appViewModel, navController, "Running") }

                composable(AppScreens.Cycling.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                ) { AcitivtyList(appViewModel, navController, "Cycling") }

                composable(AppScreens.Stats.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                ) { Stats(appViewModel, navController) }

                composable(AppScreens.Add.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                ) { AddActivity(appViewModel, navController) }

                composable(AppScreens.ActivityView.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                ) { RouteView(appViewModel, navController) }

                composable(AppScreens.Edit.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                ) { EditActivity(appViewModel, navController) }
            }
        }
    }
    else {
        Scaffold(
            floatingActionButton = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                if (navBackStackEntry?.destination?.route != AppScreens.Stats.route && showAddButton) {
                    Button(navController, appViewModel, modifier)
                }
            }
        ){
            Row {
                NavigationRail(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    elevation = 2.dp,
                    modifier = modifier
                        .fillMaxHeight()
                        .align(Alignment.Top)
                ) {
                    NavigationRailItem(
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Walking.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.walk),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    )
                    NavigationRailItem(
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Running.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.run),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                    NavigationRailItem(
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Cycling.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.bicycle),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                    NavigationRailItem(
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Stats.route) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.stats),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                    NavigationRailItem(
                        selected = false,
                        onClick = { (context as? Activity)?.finish() },
                        icon = {
                            Icon(
                                Icons.Filled.ExitToApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                    )

                }
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.Stats.route
                ) {
                    composable(
                        AppScreens.Walking.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { AcitivtyList(appViewModel, navController, "Walking") }

                    composable(
                        AppScreens.Running.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { AcitivtyList(appViewModel, navController, "Running") }

                    composable(AppScreens.Cycling.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { AcitivtyList(appViewModel, navController, "Cycling") }

                    composable(AppScreens.Stats.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { Stats(appViewModel, navController) }

                    composable(AppScreens.Add.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { AddActivity(appViewModel, navController) }

                    composable(AppScreens.ActivityView.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { RouteView(appViewModel, navController) }

                    composable(AppScreens.Edit.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                    ) { EditActivity(appViewModel, navController) }
                }
            }
        }
    }
}



@Composable
fun Button(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {
    FloatingActionButton(
        onClick = {
            navController.navigate(AppScreens.Add.route)
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
        backgroundColor = MaterialTheme.colorScheme.primary,
    ) {
        val items = listOf(
            Design(AppScreens.Walking, painterResource(id = R.drawable.walk)),
            Design(AppScreens.Running, painterResource(id = R.drawable.run)),
            Design(AppScreens.Cycling, painterResource(id = R.drawable.bicycle)),
            Design(AppScreens.Stats, painterResource(id = R.drawable.stats))
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                icon = {
                    Icon(screen.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
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
                enabled = enableButtons,

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    context: Context,
    onLanguageChange: (Language) -> Unit,
    onThemeChange: (Int) -> Unit,
    modifier: Modifier
) {
    
    var showInfo by rememberSaveable { mutableStateOf(false) }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    var showThemes by rememberSaveable { mutableStateOf(false) }
    
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

        IconButton(onClick = { showThemes = true }) {
            Icon(
                painter = painterResource(id = R.drawable.palette),
                contentDescription = stringResource(id = R.string.info),
                tint = Color(0xFFFFFFFF)
            )
        }
            IconButton(onClick = { showInfo = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(id = R.string.info),
                    tint = Color(0xFFFFFFFF)
                )
            }
            IconButton(onClick = { showSettings = true}) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.language),
                    contentDescription = stringResource(id = R.string.settings),
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    )
    InfoDialog(showInfo) { showInfo = false }
    SettingsDialog(showSettings, onLanguageChange) { showSettings = false }
    ShowThemes(showThemes, onThemeChange) { showThemes = false }
}

