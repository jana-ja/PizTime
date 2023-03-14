package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetPizRecipeUseCase(
    private val repository: Repository
) {
    operator fun invoke(id: Long): Flow<PizRecipeEntity?> {
        return repository.findPizRecipeById(id)
    }
}