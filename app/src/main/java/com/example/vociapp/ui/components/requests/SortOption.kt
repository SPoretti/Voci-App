package com.example.vociapp.ui.components.requests

import com.example.vociapp.data.local.database.Request

data class SortOption(val label: String, val comparator: Comparator<Request>)
