package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

data class PizRecipesState(
    val pizRecipes: List<PizRecipe> = emptyList(),

)
