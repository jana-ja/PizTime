package de.janaja.piztime.feature_piz_recipes.domain.model

import java.util.UUID

data class PizRecipe(
    var title: String,
    var feature: String,
    var imageName: String,
    var prepTime: Double,
    var id: String = UUID.randomUUID().toString()
)