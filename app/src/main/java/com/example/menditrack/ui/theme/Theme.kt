package com.example.menditrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF5722),
    secondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF000000),
    primaryContainer = Color(0xFF3D3C3C),
    secondaryContainer = Color(0xFF00BCD4),
    tertiaryContainer = Color(0xFFFFC107)
)

@Composable
fun MendiTrackTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}