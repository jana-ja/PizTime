package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

data class PizRecipeDetailState (
    // TODO hier das recipe rein oder eher einzelne teile davon? title usw
    val pizRecipe: PizRecipe = PizRecipe("Dummy Title", "Dummy Feature", "Dummy Description", R.drawable.bsp_piz),
    val pizIngredients: List<PizIngredient> = listOf()
)