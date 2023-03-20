package de.janaja.piztime.feature_piz_recipes.domain.model

data class PizRecipeWithDetails(
    var title: String,
    var feature: String,
    var imageResourceId: Int,
    var prepTime: Double,
    var ingredients: List<PizIngredient>,
    var steps: List<PizStepWithIngredients>,
    var id: Long = 0
)