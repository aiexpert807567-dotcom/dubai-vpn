package com.dubaivpn.app.vpn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * DEMO state holder. It only simulates connecting so the UI can be previewed.
 * Phase 7 replaces the simulation with the real WireGuard tunnel state.
 * The VPN connection and the ad-blocking switch are fully independent.
 */
class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private var tickerJob: Job? = null

    fun onConnectButton() {
        when (_state.value.connection) {
            ConnectionState.DISCONNECTED,
            ConnectionState.ERROR,
            ConnectionState.PERMISSION_REQUIRED -> connect()
            ConnectionState.CONNECTED -> disconnect()
            ConnectionState.CONNECTING,
            ConnectionState.DISCONNECTING -> Unit
        }
    }

    fun setAdBlocking(enabled: Boolean) {
        // Only changes filtering. Never touches the VPN connection state.
        _state.update { it.copy(adBlocking = enabled) }
    }

    /** Demo helper: cycles through the error screens so they can be previewed. */
    fun previewNextErrorState() {
        _state.update {
            when (it.connection) {
                ConnectionState.DISCONNECTED -> it.copy(connection = ConnectionState.ERROR)
                ConnectionState.ERROR -> it.copy(connection = ConnectionState.PERMISSION_REQUIRED)
                ConnectionState.PERMISSION_REQUIRED -> it.copy(connection = ConnectionState.DISCONNECTED)
                else -> it
            }
        }
    }

    private fun connect() {
        viewModelScope.launch {
            _state.update { it.copy(connection = ConnectionState.CONNECTING, connectedSeconds = 0L) }
            delay(1500)
            _state.update { it.copy(connection = ConnectionState.CONNECTED) }
            startTicker()
        }
    }

    private fun disconnect() {
        tickerJob?.cancel()
        viewModelScope.launch {
            _state.update { it.copy(connection = ConnectionState.DISCONNECTING) }
            delay(800)
            _state.update { it.copy(connection = ConnectionState.DISCONNECTED, connectedSeconds = 0L) }
        }
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _state.update { it.copy(connectedSeconds = it.connectedSeconds + 1) }
            }
        }
    }
}
