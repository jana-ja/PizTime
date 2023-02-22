package de.janaja.piztime.feature_piz_recipes.domain.model


class PizIngredient(
    var ingredient: String,
    var baseAmount: Double,
    var id: Long = 0
) {
}