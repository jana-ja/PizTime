package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class GetIngredientUseCase (
    private val repository: Repository
){
    suspend operator fun invoke(id: Long, isStepIngredient: Boolean): PizIngredient {
        if(isStepIngredient) {
            return repository.getPizStepIngredient(id)
        } else {
            return repository.getPizIngredient(id)
        }
    }
}