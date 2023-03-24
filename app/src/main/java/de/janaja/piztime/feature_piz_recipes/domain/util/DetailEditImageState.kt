package de.janaja.piztime.feature_piz_recipes.domain.util

import androidx.compose.ui.graphics.ImageBitmap

data class DetailEditImageState (
    val imageName: String = "",
    val bitmap: ImageBitmap? = null
)