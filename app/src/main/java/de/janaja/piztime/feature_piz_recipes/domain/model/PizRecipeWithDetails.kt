package de.janaja.piztime.feature_piz_recipes.domain.model

import java.util.UUID

data class PizRecipeWithDetails(
    var title: String,
    var feature: String,
    var imageName: String,
    var prepTime: Double,
    var ingredients: List<PizIngredient>,
    var steps: List<PizStepWithIngredients>,
    var id: String = UUID.randomUUID().toString()
)