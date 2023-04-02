package de.janaja.piztime.feature_piz_recipes.domain.util

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails

data class DetailPizRecipeWithDetailsState (
    val pizRecipe: PizRecipeWithDetails? = null,
    val imageBitmap: ImageBitmap? = null,
    val firstLaunch: Boolean = true // for animation
)