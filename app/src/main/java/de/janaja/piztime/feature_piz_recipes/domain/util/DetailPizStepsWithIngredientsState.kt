package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients

data class DetailPizStepsWithIngredientsState (
    val pizStepsWithIngredients: List<PizStepWithIngredients> = listOf()
)