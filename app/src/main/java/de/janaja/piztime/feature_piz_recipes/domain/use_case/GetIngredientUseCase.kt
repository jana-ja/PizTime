package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class GetIngredientUseCase (
    private val repository: Repository
){
    suspend operator fun invoke(id: String, recipeId: String, stepId: String?, isStepIngredient: Boolean): PizIngredient? {
        return if(isStepIngredient) {
            if(stepId != null)
                repository.getPizStepIngredient(id, recipeId, stepId)
            else
                null
        } else {
            repository.getPizIngredient(id, recipeId)
        }
    }
}