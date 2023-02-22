package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity

data class DetailPizIngredientsState (
    val pizIngredientEntities: List<PizIngredientEntity> = listOf(),
)