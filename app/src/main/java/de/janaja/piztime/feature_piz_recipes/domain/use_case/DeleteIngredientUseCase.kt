package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class DeleteIngredientUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: String, isStepIngredient: Boolean) {
        if(isStepIngredient){
            repository.deletePizStepIngredient(id)
        } else {
            repository.deletePizIngredient(id)
        }
    }
}