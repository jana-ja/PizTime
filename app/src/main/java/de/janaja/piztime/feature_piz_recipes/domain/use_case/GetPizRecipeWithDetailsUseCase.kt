package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetPizRecipeWithDetailsUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long): PizRecipeWithDetails? {
       repository.findPizRecipeWithDetailsById(id)
    }
}