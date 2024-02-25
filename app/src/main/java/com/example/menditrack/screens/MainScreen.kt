package com.example.menditrack.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController, appViewModel: AppViewModel, modifier: Modifier){
    Scaffold (
        topBar = { TopBar(navController, appViewModel, modifier)},
        bottomBar = { BottomBar(navController, appViewModel, modifier) },
        floatingActionButton = { FloatingButton(navController, appViewModel, modifier) }
    ) {
        BodyContent(navController, appViewModel, modifier)
    }
}

@Composable
fun FloatingButton(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {

}

@Composable
fun BottomBar(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Filled.List,
                contentDescription = stringResource(id = R.string.public_routes),
                tint = Color(0xFFFFFFFF)
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = stringResource(id = R.string.my_routes),
                tint = Color(0xFFFFFFFF)
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(id = R.string.my_profile),
                tint = Color(0xFFFFFFFF)
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Filled.ExitToApp,
                contentDescription = stringResource(id = R.string.log_out),
                tint = Color(0xFFFFFFFF)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.goBack),
                    tint = Color(0xFFFFFFFF)
                )
            }
        },

        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(id = R.string.info),
                    tint = Color(0xFFFFFFFF)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(id = R.string.settings),
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    )
}

@Composable
fun BodyContent(navController: NavController, appViewModel: AppViewModel, modifier: Modifier) {


}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MainScreenPreviewLight(){
    MainScreen(navController = rememberNavController(), appViewModel = AppViewModel(), modifier = Modifier)
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreviewDark(){
    MainScreen(navController = rememberNavController(), appViewModel = AppViewModel(), modifier = Modifier)
}