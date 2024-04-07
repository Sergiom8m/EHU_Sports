package com.example.menditrack.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DrawerValue
import androidx.compose.material.NavigationRail
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.rememberDrawerState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.viewModel.AppViewModel
import com.example.menditrack.utils.InfoDialog
import com.example.menditrack.viewModel.PreferencesViewModel
import com.example.menditrack.R
import com.example.menditrack.utils.SettingsDialog
import com.example.menditrack.utils.ShowThemes
import com.example.menditrack.data.Design
import com.example.menditrack.data.Language
import com.example.menditrack.navigation.AppScreens
import com.example.menditrack.utils.ShowDeleteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    prefViewModel: PreferencesViewModel,
    pickMedia: ActivityResultLauncher<PickVisualMediaRequest>,
    modifier: Modifier,
){
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Define language and theme change functions to use them inside composables
    val onLanguageChange:(Language)-> Unit = { prefViewModel.changeLang(it) }
    val onThemeChange:(Int)-> Unit = { prefViewModel.changeTheme(it) }

    val onEditProfile: () -> Unit = {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        CoroutineScope(Dispatchers.Main).launch { drawerState.close() }
    }

    // Create a navController
    val navController = rememberNavController()

    // Get variables to manage the visibility of the nav bars
    val showAddButton = appViewModel.showAddButton
    val showSettingButton = appViewModel.showNavBars

    // Get information about screen orientation
    val configuration = LocalConfiguration.current
    val isVertical = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // If the orientation is vertical show portrait design
    Scaffold(
        // TOP BAR
        topBar = {
            if (isVertical) {
                // Manage the top bar's visibility with ViewModel's variables (ANIMATED ENTERING/EXITING)
                AnimatedVisibility(
                    visible = showSettingButton,
                    enter = slideInVertically(initialOffsetY = { it }) + expandVertically(),
                    exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
                ) { TopBar(appViewModel, context, onEditProfile, modifier) }
            }
        },
        // BOTTOM BAR
        bottomBar = {
            if (isVertical) {
                // Manage the bottom bar's visibility with ViewModel's variables (ANIMATED ENTERING/EXITING)
                AnimatedVisibility(
                    visible = showSettingButton,
                    enter = slideInVertically(initialOffsetY = { it }) + expandVertically(),
                    exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
                ) { BottomBar(navController) }
            }
        },
        // FLOATING BUTTON
        floatingActionButton = {
            // Retrieve the current back stack entry
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            // Check if the current destination of the navigation back stack is not the Stats screen and
            // if the showAddButton flag is true.
            if (navBackStackEntry?.destination?.route != AppScreens.Stats.route && showAddButton) {
                AddButton(navController)
            }
        }
    ) { innerPadding ->

        Row {
            if (!isVertical){
                NavRail(navController, context)
            }
            // Display the NavHost in the content of the scaffold (innerPadding to keep it inside the content site)
            MainNavHost(navController, appViewModel, innerPadding)
        }
        // Invoke dialog components with the "show" parameter and set it to false after the dialogs are dismissed.
        InfoDialog(appViewModel.showInfo, context) { appViewModel.showInfo = false }
        SettingsDialog(appViewModel.showSettings, onLanguageChange) { appViewModel.showSettings = false }
        ShowThemes(appViewModel.showThemes, onThemeChange) { appViewModel.showThemes = false }
        ShowDeleteMessage(appViewModel.showDelete, appViewModel) { appViewModel.showDelete = false }
    }

}



// Composable function to define the floating add button
@Composable
fun AddButton(navController: NavController) {
    FloatingActionButton(
        onClick = {
            // When add button is clicked led user to add screen
            navController.navigate(AppScreens.Add.route)
          },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape
    ) {
        Icon(Icons.Filled.Add, stringResource(id = R.string.add))
    }
}


// Composable function to define the bottom navigation bar
@Composable
fun BottomBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary,
    ) {
        // Define a list with design objects containing screen objects and their icons
        val screens = listOf(
            Design(AppScreens.Walking, painterResource(id = R.drawable.walk)),
            Design(AppScreens.Running, painterResource(id = R.drawable.run)),
            Design(AppScreens. Cycling, painterResource(id = R.drawable.bicycle)),
            Design(AppScreens.Stats, painterResource(id = R.drawable.stats))
        )

        // Retrieve the current back stack entry
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // Retrieve the current destination from the navigation back stack entry
        val currentDestination = navBackStackEntry?.destination

        // Iterate the existing screens to crate a icon in the bottom navigation bar
        screens.forEach { screen ->
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
                    // Navigate to the specified screen's route using the NavController.
                    navController.navigate(screen.screen.route) {
                        /// Clear the back stack up to the start destination of the navigation graph.
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Specify that the destination should be launched as a single top-level destination.
                        launchSingleTop = true
                        // Specify whether to restore the state of the destination if it already exists in the back stack.
                        restoreState = true
                    }
                },
            )
        }
    }
}

// Composable function to define top navigation bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    appViewModel: AppViewModel,
    context: Context,
    onEditProfile: () -> Unit,
    modifier: Modifier
) {

    val profilePicture = appViewModel.profilePicture

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text( text = stringResource(id = R.string.app_name))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color(0xFFFFFFFF),
        ),
        // Button to exit the app
        navigationIcon = {
            IconButton(onClick = { (context as? Activity)?.finish() }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = stringResource(id = R.string.go_back),
                    tint = Color(0xFFFFFFFF)
                )
            }
        },
        // Action buttons
        actions = {

            // Button to show available themes dialog
            IconButton(onClick = { appViewModel.showThemes = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.palette),
                    contentDescription = stringResource(id = R.string.info),
                    tint = Color(0xFFFFFFFF)
                )
            }
            // Button to show application information dialog
            IconButton(onClick = { appViewModel.showInfo = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(id = R.string.info),
                    tint = Color(0xFFFFFFFF)
                )
            }
            // Button to show available languages dialog
            IconButton(onClick = { appViewModel.showSettings = true}) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.language),
                    contentDescription = stringResource(id = R.string.settings),
                    tint = Color(0xFFFFFFFF)
                )
            }
            IconButton(onClick = { onEditProfile() }) {
                if(profilePicture == null) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = stringResource(id = R.string.settings),
                        tint = Color(0xFFFFFFFF),
                        modifier = modifier.size(55.dp)
                    )
                }
                else{
                    Image(
                        bitmap = profilePicture.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape),
                    )
                }
            }
        }
    )
}

// A composable function to define the navigation host (COMMON FOR LANDSCAPE AND PORTRAIT)
@Composable
fun MainNavHost(
    navController: NavHostController,
    appViewModel: AppViewModel,
    innerPadding: PaddingValues
){
    NavHost(
        navController = navController,
        startDestination = AppScreens.Stats.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        // Define composable destinations for different screens with transitions

        composable(
            AppScreens.Walking.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { ActivityList(appViewModel, navController, "Walking") }

        composable(
            AppScreens.Running.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { ActivityList(appViewModel, navController, "Running") }

        composable(AppScreens.Cycling.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { ActivityList(appViewModel, navController, "Cycling") }

        composable(AppScreens.Stats.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { Stats(appViewModel) }

        composable(AppScreens.Add.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { AddActivity(appViewModel, navController) }

        composable(AppScreens.ActivityView.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { ViewActivity(appViewModel, navController) }

        composable(AppScreens.Edit.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { EditActivity(appViewModel, navController) }

        composable(AppScreens.Map.route,
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(1000)) }
        ) { MapScreen(appViewModel, navController) }
    }
}

// Composable function to define the Navigation Rain shown in the landscape design
@Composable
fun NavRail(navController: NavController, context: Context){
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NavigationRail(
            backgroundColor = MaterialTheme.colorScheme.primary,
            elevation = 2.dp
        ) {
            // Define each item in the navigation rail
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

            Spacer(modifier = Modifier.weight(1f))

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
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}