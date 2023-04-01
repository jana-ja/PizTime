package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class DeleteIngredientUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: String, recipeId: String, stepId: String?, isStepIngredient: Boolean) {
        if(isStepIngredient){
            if(stepId != null)
                repository.deletePizStepIngredient(id, recipeId, stepId)
            //else
                // TODO error
        } else {
            repository.deletePizIngredient(id, recipeId)
        }
    }
}