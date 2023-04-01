package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateIngredientUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(pizIngredient: PizIngredient, isStepIngredient: Boolean, mapId: String) {
        if(isStepIngredient){
            repository.insertPizStepIngredient(pizIngredient, mapId)
        } else {
            repository.insertPizIngredient(pizIngredient, mapId)
        }
    }
}