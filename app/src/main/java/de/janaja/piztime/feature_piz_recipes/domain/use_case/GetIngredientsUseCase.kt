package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetIngredientsUseCase (
    private val repository: Repository
){
    operator fun invoke(id: Long): Flow<List<PizIngredient>> {
        return repository.findPizIngredientsByPizRecipeId(id)
    }
}