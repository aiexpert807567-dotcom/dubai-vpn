package com.dubaivpn.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dubaivpn.app.ui.components.InfoRow
import com.dubaivpn.app.ui.components.SectionCard
import com.dubaivpn.app.ui.theme.DvColors
import com.dubaivpn.app.vpn.ConnectionState
import com.dubaivpn.app.vpn.HomeViewModel

@Composable
fun HomeScreen(vm: HomeViewModel = viewModel()) {
    val ui by vm.state.collectAsState()
    val connection = ui.connection
    val busy = connection == ConnectionState.CONNECTING || connection == ConnectionState.DISCONNECTING

    val stateColor by animateColorAsState(
        targetValue = when (connection) {
            ConnectionState.CONNECTED -> DvColors.Connected
            ConnectionState.CONNECTING, ConnectionState.DISCONNECTING -> DvColors.Connecting
            ConnectionState.ERROR, ConnectionState.PERMISSION_REQUIRED -> DvColors.Error
            ConnectionState.DISCONNECTED -> DvColors.Idle
        },
        animationSpec = tween(500),
        label = "state-color"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "DUBAI VPN",
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp,
            color = DvColors.TextPrimary
        )

        if (ui.demoMode) {
            Spacer(Modifier.height(8.dp))
            DemoChip(onPreviewError = vm::previewNextErrorState)
        }

        Spacer(Modifier.height(24.dp))
        StatusLine(text = connection.label(), color = stateColor)

        Spacer(Modifier.height(24.dp))
        ConnectButton(
            label = connection.buttonLabel(),
            color = stateColor,
            busy = busy,
            onClick = vm::onConnectButton
        )

        val message = connection.message()
        if (message != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = message,
                color = DvColors.TextMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(24.dp))
        SectionCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\uD83D\uDEE1  Ad Blocking",
                        color = DvColors.TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (ui.adBlocking) "ON \u2013 blocking ads and trackers"
                        else "OFF \u2013 filtering paused, DNS still works",
                        color = DvColors.TextMuted,
                        fontSize = 13.sp
                    )
                }
                Switch(
                    checked = ui.adBlocking,
                    onCheckedChange = vm::setAdBlocking,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = DvColors.TextPrimary,
                        checkedTrackColor = DvColors.Accent,
                        uncheckedThumbColor = DvColors.TextPrimary,
                        uncheckedTrackColor = DvColors.Idle,
                        uncheckedBorderColor = DvColors.Idle
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        SectionCard {
            InfoRow("Server", ui.serverName)
            // Real numbers arrive in the statistics phase. Never show invented values.
            InfoRow("Data used", "Not measured (demo)")
            if (connection == ConnectionState.CONNECTED) {
                InfoRow("Connected", formatDuration(ui.connectedSeconds))
            }
        }

        // Space so the floating "Contact developer" button never covers content.
        Spacer(Modifier.height(110.dp))
    }
}

@Composable
private fun DemoChip(onPreviewError: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(DvColors.CardSoft)
            .padding(start = 14.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("DEMO \u00B7 no real VPN yet", color = DvColors.TextMuted, fontSize = 12.sp)
        TextButton(onClick = onPreviewError) {
            Text("Preview error states", color = DvColors.Link, fontSize = 12.sp)
        }
    }
}

@Composable
private fun StatusLine(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            color = DvColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun ConnectButton(label: String, color: Color, busy: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
        if (busy) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = DvColors.TextPrimary,
                strokeWidth = 4.dp
            )
        }
        Box(
            modifier = Modifier
                .size(184.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(color)
                .clickable(enabled = !busy, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = DvColors.OnAccent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

private fun ConnectionState.label(): String = when (this) {
    ConnectionState.DISCONNECTED -> "DISCONNECTED"
    ConnectionState.CONNECTING -> "CONNECTING\u2026"
    ConnectionState.CONNECTED -> "CONNECTED"
    ConnectionState.DISCONNECTING -> "DISCONNECTING\u2026"
    ConnectionState.ERROR -> "ERROR"
    ConnectionState.PERMISSION_REQUIRED -> "PERMISSION REQUIRED"
}

private fun ConnectionState.buttonLabel(): String = when (this) {
    ConnectionState.DISCONNECTED -> "CONNECT"
    ConnectionState.CONNECTING -> "CONNECTING"
    ConnectionState.CONNECTED -> "DISCONNECT"
    ConnectionState.DISCONNECTING -> "DISCONNECTING"
    ConnectionState.ERROR -> "TRY AGAIN"
    ConnectionState.PERMISSION_REQUIRED -> "ALLOW VPN"
}

private fun ConnectionState.message(): String? = when (this) {
    ConnectionState.ERROR -> "Unable to reach VPN server.\nCheck your Internet connection."
    ConnectionState.PERMISSION_REQUIRED -> "VPN permission is required to connect."
    else -> null
}

private fun formatDuration(totalSeconds: Long): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}
