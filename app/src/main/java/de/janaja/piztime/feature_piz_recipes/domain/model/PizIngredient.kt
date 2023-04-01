package de.janaja.piztime.feature_piz_recipes.domain.model

import java.util.UUID


data class PizIngredient(
    var ingredient: String,
    var baseAmount: Double,
    var id: String = UUID.randomUUID().toString()
) {
}