package com.voci.app.ui.components.requests

import com.voci.app.data.local.database.Request

data class SortOption(val label: String, val comparator: Comparator<Request>)
