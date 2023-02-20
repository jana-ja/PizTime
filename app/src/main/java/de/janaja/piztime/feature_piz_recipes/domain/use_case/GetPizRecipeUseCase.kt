package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetPizRecipeUseCase(
    private val repository: Repository
) {
    operator fun invoke(id: Long): Flow<PizRecipe?> {
        return repository.findPizRecipebyId(id)
    }
}