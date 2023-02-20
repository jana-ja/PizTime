package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.*

data class DetailPizStepsWithIngredientsState (
    val pizStepsWithIngredients: List<Pair<PizStep, List<PizStepIngredient>>> = listOf()
)