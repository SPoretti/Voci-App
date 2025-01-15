package com.example.vociapp.ui.components.homeless

import com.example.vociapp.data.local.database.Homeless

data class HomelessItemUiState (
    val homeless: Homeless,
    var isPreferred: Boolean = false,
)