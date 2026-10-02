package com.dubaivpn.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** All app colors live here. Soft slate-blue theme: not too dark, no neon. */
object DvColors {
    val Background = Color(0xFF2B3E55)
    val Card = Color(0xFF36506B)
    val CardSoft = Color(0xFF3F5C7A)
    val Accent = Color(0xFF5FB8A5)
    val Idle = Color(0xFF8FA6C0)
    val Connected = Color(0xFF72C39A)
    val Connecting = Color(0xFFE3B866)
    val Error = Color(0xFFE58B7E)
    val TextPrimary = Color(0xFFF4F7FA)
    val TextMuted = Color(0xFFC9D4E0)
    val OnAccent = Color(0xFF14212E)
    val Link = Color(0xFF8ADBC8)
    val WhatsApp = Color(0xFF25D366)
}

@Composable
fun DubaiVpnTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = DvColors.Accent,
            onPrimary = DvColors.OnAccent,
            background = DvColors.Background,
            onBackground = DvColors.TextPrimary,
            surface = DvColors.Card,
            onSurface = DvColors.TextPrimary,
            onSurfaceVariant = DvColors.TextMuted,
            surfaceContainerHigh = DvColors.Card
        ),
        content = content
    )
}
