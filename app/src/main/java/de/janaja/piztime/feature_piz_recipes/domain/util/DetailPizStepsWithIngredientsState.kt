package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

data class DetailPizStepsWithIngredientsState (
    val pizStepsWithIngredientsDto: List<Pair<PizStepEntity, List<PizStepIngredientEntity>>> = listOf()
)