package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizStep
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetStepsWithIngredientsUseCase (
    private val repository: Repository
){
    operator fun invoke(pizRecipeId: Long): Flow<Map<PizStep, List<PizStepIngredient>>> {
        return repository.findPizStepsWithIngredientsByPizRecipeId(pizRecipeId)
    }
}