package de.janaja.piztime.feature_piz_recipes.domain.model


data class PizStepWithIngredients(
    var description: String,
    var ingredients: List<PizIngredient>,
    var id: Long = 0
) {

}