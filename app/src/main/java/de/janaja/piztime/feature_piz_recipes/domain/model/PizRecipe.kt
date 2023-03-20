package de.janaja.piztime.feature_piz_recipes.domain.model

data class PizRecipe(
    var title: String,
    var feature: String,
    var imageResourceId: Int,
    var prepTime: Double,
    var id: Long = 0
)