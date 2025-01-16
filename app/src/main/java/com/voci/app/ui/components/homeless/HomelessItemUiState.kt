package com.voci.app.ui.components.homeless

import com.voci.app.data.local.database.Homeless

data class HomelessItemUiState (
    val homeless: Homeless,
    var isPreferred: Boolean = false,
)