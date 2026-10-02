package com.dubaivpn.app.vpn

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
    ERROR,
    PERMISSION_REQUIRED
}

data class HomeUiState(
    val connection: ConnectionState = ConnectionState.DISCONNECTED,
    val adBlocking: Boolean = true,
    val serverName: String = "Europe",
    val connectedSeconds: Long = 0L,
    // True until Phase 7 replaces the simulation with the real WireGuard tunnel.
    val demoMode: Boolean = true
)
