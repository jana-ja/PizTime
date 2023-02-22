package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetIngredientsUseCase (
    private val repository: Repository
){
    operator fun invoke(id: Long): Flow<List<PizIngredientEntity>> {
        return repository.findPizIngredientsByPizRecipeId(id)
    }
}