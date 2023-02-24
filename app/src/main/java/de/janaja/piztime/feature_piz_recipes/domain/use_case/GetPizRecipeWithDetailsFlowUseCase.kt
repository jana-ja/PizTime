package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class GetPizRecipeWithDetailsFlowUseCase(
    private val repository: Repository
) {
    operator fun invoke(): StateFlow<PizRecipeWithDetails> {
       return repository.pizRecipeWithDetailsFlow
    }
}