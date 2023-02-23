package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

data class DetailPizRecipeWithDetailsState (
    val pizRecipe: PizRecipeWithDetails = DummyData.DummyPizRecipeWithDetails
)