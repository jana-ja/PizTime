package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients

data class EditPizStepsWithIngredientsState (
    val stepsWithIngredients: List<PizStepWithIngredients> = listOf(),
    val stepDescriptions: List<String> = listOf(),
    val ingredients: List<List<PizIngredient>> = listOf(), // TODO move to own state
)