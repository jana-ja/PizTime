package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

data class DetailPizRecipeState (
    val pizRecipe: PizRecipe = PizRecipe("Dummy Title", "Dummy Feature", R.drawable.bsp_piz),
)