package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class LoadPizRecipeWithDetailsUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long) {
       repository.findPizRecipeWithDetailsById(id)
    }
}