package com.example.menditrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.menditrack.viewModel.PreferencesViewModel

// Definition of the different themes available in the application
private val OrangeColorScheme = lightColorScheme(
    primary = Color(0xFFFF5722),
    secondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF000000),
    primaryContainer = Color(0xFF3D3C3C),
    secondaryContainer = Color(0xFF00BCD4),
    tertiaryContainer = Color(0xFFFFC107)
)

private val BlueColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    secondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF4CAF50),
    primaryContainer = Color(0xFF3F51B5),
    secondaryContainer = Color(0xFFFF5722),
    tertiaryContainer = Color(0xFFFFC107)
)

private val GreenColorScheme = lightColorScheme(
    primary = Color(0xFF6C733D),
    secondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF4CAF50),
    primaryContainer = Color(0xFF202426),
    secondaryContainer = Color(0xFFFF5722),
    tertiaryContainer = Color(0xFFFFC107)
)

@Composable
fun MendiTrackTheme(
    prefViewModel: PreferencesViewModel,
    content: @Composable () -> Unit
) {
    // Get the variable that indicates the selected theme (Default orange)
    val theme by prefViewModel.theme.collectAsState(initial = 0)

    // Set the theme depending on the variable's value
    when (theme) {
        0 -> {
            MaterialTheme(
                colorScheme = OrangeColorScheme,
                typography = Typography,
                content = content
            )
        }
        1 -> {
            MaterialTheme(
                colorScheme = BlueColorScheme,
                typography = Typography,
                content = content
            )
        }
        else -> {
            MaterialTheme(
                colorScheme = GreenColorScheme,
                typography = Typography,
                content = content
            )
        }
    }
}