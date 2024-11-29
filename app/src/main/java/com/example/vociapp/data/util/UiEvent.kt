package com.example.vociapp.data.util

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    // Add other UI events here if needed
}