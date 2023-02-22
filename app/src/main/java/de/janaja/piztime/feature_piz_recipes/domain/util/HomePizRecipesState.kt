package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity

data class HomePizRecipesState(
    val pizRecipeEntities: List<PizRecipeEntity> = emptyList(),

    )
