package de.janaja.piztime.feature_piz_recipes.domain.util

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity

data class DetailPizRecipeState (
    val pizRecipeEntity: PizRecipeEntity = PizRecipeEntity("Dummy Title", "Dummy Feature", R.drawable.bsp_piz),
)