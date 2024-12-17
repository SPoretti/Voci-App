package com.example.vociapp.ui.state

import com.example.vociapp.data.types.Homeless

data class HomelessItemUiState (
    val homeless: Homeless,
    var isPreferred: Boolean = false,
)