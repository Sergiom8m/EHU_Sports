package com.example.menditrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.menditrack.PreferencesViewModel


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF5722),
    secondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF000000),
    primaryContainer = Color(0xFF3D3C3C),
    secondaryContainer = Color(0xFF00BCD4),
    tertiaryContainer = Color(0xFFFFC107)
)

private val AlternativeColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    secondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF4CAF50),
    primaryContainer = Color(0xFF3F51B5),
    secondaryContainer = Color(0xFFFF5722),
    tertiaryContainer = Color(0xFFFFC107)
)

private val TertiaryColorScheme = lightColorScheme(
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

    val theme by prefViewModel.theme.collectAsState(initial = 0)

    when (theme) {
        0 -> {
            MaterialTheme(
                colorScheme = LightColorScheme,
                typography = Typography,
                content = content
            )
        }
        1 -> {
            MaterialTheme(
                colorScheme = AlternativeColorScheme,
                typography = Typography,
                content = content
            )
        }
        else -> {
            MaterialTheme(
                colorScheme = TertiaryColorScheme,
                typography = Typography,
                content = content
            )
        }
    }
}