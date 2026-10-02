package com.dubaivpn.app

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.dubaivpn.app.ui.components.ContactDeveloperButton
import com.dubaivpn.app.ui.components.ContactDeveloperDialog
import com.dubaivpn.app.ui.screens.AboutScreen
import com.dubaivpn.app.ui.screens.DevicesScreen
import com.dubaivpn.app.ui.screens.HomeScreen
import com.dubaivpn.app.ui.screens.SettingsScreen
import com.dubaivpn.app.ui.theme.DubaiVpnTheme
import com.dubaivpn.app.ui.theme.DvColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        setContent {
            DubaiVpnTheme {
                DubaiVpnApp()
            }
        }
    }
}

enum class AppTab(val title: String, val glyph: String) {
    HOME("Home", "\uD83C\uDFE0"),
    SETTINGS("Settings", "\u2699\uFE0F"),
    DEVICES("Devices", "\uD83D\uDCF1"),
    ABOUT("About", "\u2139\uFE0F")
}

@Composable
fun DubaiVpnApp() {
    var tab by rememberSaveable { mutableStateOf(AppTab.HOME) }
    var showContact by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = DvColors.Background,
        contentColor = DvColors.TextPrimary,
        floatingActionButton = { ContactDeveloperButton(onClick = { showContact = true }) },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            NavigationBar(containerColor = DvColors.Card) {
                AppTab.entries.forEach { item ->
                    NavigationBarItem(
                        selected = tab == item,
                        onClick = { tab = item },
                        icon = { Text(item.glyph, fontSize = 20.sp) },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = DvColors.TextPrimary,
                            unselectedTextColor = DvColors.TextMuted,
                            indicatorColor = DvColors.Accent.copy(alpha = 0.35f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (tab) {
                AppTab.HOME -> HomeScreen()
                AppTab.SETTINGS -> SettingsScreen()
                AppTab.DEVICES -> DevicesScreen()
                AppTab.ABOUT -> AboutScreen(onContact = { showContact = true })
            }
        }
    }

    if (showContact) {
        ContactDeveloperDialog(onDismiss = { showContact = false })
    }
}
