package de.janaja.piztime.feature_piz_recipes.domain.util

data class DetailEditIngredientState (
    val id: String = "",
    val ingredientName: String = "",
    val ingredientAmount: String = "",
    val isStepIngredient: Boolean = false,
    val stepId: String? = ""
)