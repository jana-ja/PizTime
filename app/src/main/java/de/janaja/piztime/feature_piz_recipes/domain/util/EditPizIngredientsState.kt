package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient

data class EditPizIngredientsState (
    val pizIngredients: List<PizIngredient> = listOf(),
)