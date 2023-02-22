package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetStepsWithIngredientsUseCase (
    private val repository: Repository
){
    operator fun invoke(pizRecipeId: Long): Flow<Map<PizStepEntity, List<PizStepIngredientEntity>>> {
        return repository.findPizStepsWithIngredientsByPizRecipeId(pizRecipeId)
    }
}