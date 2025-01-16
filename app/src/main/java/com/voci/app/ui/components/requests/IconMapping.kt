package com.voci.app.ui.components.requests

import com.voci.app.R
import com.voci.app.data.util.IconCategory

// Mapping between IconCategory and Icons
val iconCategoryMap: Map<IconCategory, Int> = mapOf(
    IconCategory.SHOES to R.drawable.shoes_icon,
    IconCategory.PANTS to R.drawable.pants_icon,
    IconCategory.SHIRT to R.drawable.shirt_icon,
    IconCategory.UNDERWEAR to R.drawable.underwear_icon,
    IconCategory.CAP to R.drawable.cap_icon,
    IconCategory.OTHER to R.drawable.more_icon
)