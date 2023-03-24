package de.janaja.piztime.feature_piz_recipes.domain.util

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

data class DetailPizRecipeWithDetailsState (
    val pizRecipe: PizRecipeWithDetails = DummyData.DummyPizRecipeWithDetails,
    val imageBitmap: ImageBitmap? = null,
    val firstLaunch: Boolean = true
)