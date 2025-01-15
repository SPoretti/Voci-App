package com.example.vociapp.ui.components.volunteers

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

object SnackbarManager {
    private val snackbarHostState = SnackbarHostState()

    suspend fun showSnackbar(message: String, actionLabel: String? = null) {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Short
        )
    }

    @Composable
    fun getSnackbarHostState(): SnackbarHostState {
        return remember { snackbarHostState }
    }

    @Composable
    fun CustomSnackbarHost(isBottomBarVisible: Boolean = false) {
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { snackbarData ->
                CustomSnackbar(snackbarData, isBottomBarVisible)
            }
        )
    }
}
