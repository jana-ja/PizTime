package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients

data class EditPizStepsWithIngredientsState (
    val stepsWithIngredients: List<PizStepWithIngredients> = listOf(),
    val stepDescriptions: List<String> = listOf(),
    val stepIngredients: List<List<PizIngredient>> = listOf(),
    val stepIngredientsNames: List<List<String>> = listOf(), // TODO move to own state
    val stepIngredientsAmounts: List<List<Double>> = listOf(), // TODO move to own state
)