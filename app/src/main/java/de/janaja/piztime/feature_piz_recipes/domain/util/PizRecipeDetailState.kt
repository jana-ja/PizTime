package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

data class PizRecipeDetailState (
    // TODO hier das recipe rein oder eher einzelne teile davon? title usw
    val pizRecipe: PizRecipe? = null,
    val amount: Int = 1
)