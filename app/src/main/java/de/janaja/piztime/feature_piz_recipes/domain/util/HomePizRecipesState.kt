package de.janaja.piztime.feature_piz_recipes.domain.util

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

data class HomePizRecipesState(
    val pizRecipes: List<PizRecipe> = emptyList(),
    val recipeImages: List<ImageBitmap?> = emptyList()
    )
