package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class ResetPizRecipeWithDetailsFlowUseCase(
    private val repository: Repository
) {
    operator fun invoke() {
        repository.resetRecipeWithDetailsFlow()
    }
}