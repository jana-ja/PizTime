package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient

data class DetailPizIngredientsState (
    val pizIngredients: List<PizIngredient> = listOf(),
)