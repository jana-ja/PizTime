package de.janaja.piztime.feature_piz_recipes.domain.model

import java.util.UUID


data class PizStepWithIngredients(
    var description: String,
    var ingredients: List<PizIngredient>,
    var id: String = UUID.randomUUID().toString()
) {

}