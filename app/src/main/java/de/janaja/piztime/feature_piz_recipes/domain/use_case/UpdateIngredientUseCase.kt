package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateIngredientUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(pizIngredient: PizIngredient, recipeId: String, stepId: String?, isStepIngredient: Boolean) {
        if(isStepIngredient){
            if (stepId != null)
                repository.insertPizStepIngredient(pizIngredient, recipeId, stepId)
            //else
                // TODO error
        } else {
            repository.insertPizIngredient(pizIngredient, recipeId)
        }
    }
}