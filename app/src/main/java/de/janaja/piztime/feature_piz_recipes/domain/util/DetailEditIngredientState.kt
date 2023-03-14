package de.janaja.piztime.feature_piz_recipes.domain.util

data class DetailEditIngredientState (
    val id: Long = -1,
    val ingredientName: String = "",
    val ingredientAmount: String = "",
    val isStepIngredient: Boolean = false,
    val mapId: Long = -1
)