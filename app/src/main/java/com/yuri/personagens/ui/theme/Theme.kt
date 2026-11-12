package com.yuri.personagens.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = corTextoButton,
    background = corBackgroundVerde,
    surface = corCampoBranco,
    onBackground = Color.White,
    onSurface = Color.Black
)

@Composable
fun PersonagensTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}