package com.dubaivpn.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dubaivpn.app.ui.components.Contact
import com.dubaivpn.app.ui.components.InfoRow
import com.dubaivpn.app.ui.components.ScreenTitle
import com.dubaivpn.app.ui.components.SectionCard
import com.dubaivpn.app.ui.theme.DvColors

@Composable
private fun ScreenColumn(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        ScreenTitle(title)
        content()
        // Space so the floating "Contact developer" button never covers content.
        Spacer(Modifier.height(110.dp))
    }
}

@Composable
fun SettingsScreen() {
    ScreenColumn("Settings") {
        SectionCard {
            InfoRow("Server", "Europe")
            InfoRow("Auto-connect", "Coming soon")
            InfoRow("Notifications", "Coming soon")
            InfoRow("Kill switch", "Optional, later")
            InfoRow("Theme", "Soft slate")
        }
    }
}

@Composable
fun DevicesScreen() {
    ScreenColumn("Devices") {
        SectionCard {
            Text("Current device", color = DvColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            InfoRow("This phone", "Not registered yet")
        }
        Spacer(Modifier.height(16.dp))
        SectionCard {
            Text("Other authorized devices", color = DvColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            InfoRow("None yet", "")
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Device registration and revoking arrive in a later phase.",
            color = DvColors.TextMuted,
            fontSize = 13.sp
        )
    }
}

@Composable
fun AboutScreen(onContact: () -> Unit) {
    ScreenColumn("About") {
        SectionCard {
            Text("Dubai VPN", color = DvColors.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            InfoRow("Version", "0.1.0 (preview)")
        }
        Spacer(Modifier.height(16.dp))
        SectionCard {
            Text("Privacy", color = DvColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "A VPN encrypts traffic between your device and the VPN server. " +
                    "It does not guarantee complete anonymity.",
                color = DvColors.TextMuted,
                fontSize = 14.sp
            )
        }
        Spacer(Modifier.height(16.dp))
        SectionCard {
            Text("Open-source components", color = DvColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text("WireGuard and AdGuard Home will be listed here as they are added.", color = DvColors.TextMuted, fontSize = 14.sp)
        }
        Spacer(Modifier.height(16.dp))
        SectionCard {
            Text("Developer", color = DvColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            InfoRow("WhatsApp", Contact.DISPLAY_NUMBER)
            Button(
                onClick = onContact,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DvColors.WhatsApp,
                    contentColor = DvColors.OnAccent
                )
            ) {
                Text("Contact developer", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
