package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient

data class EditPizIngredientsState (
    val pizIngredients: List<PizIngredient> = listOf(),
    val ingredientNames: List<String> = listOf(), //SnapshotStateList<String> = mutableStateListOf(), // TODO probieren mit get // TODO move to own state
    val ingredientAmounts: List<Double> = listOf(),
)