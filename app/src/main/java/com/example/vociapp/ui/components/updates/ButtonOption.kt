package com.example.vociapp.ui.components.updates

sealed class ButtonOption {
    object Green : ButtonOption()
    object Yellow : ButtonOption()
    object Red : ButtonOption()
    object Gray : ButtonOption()
}