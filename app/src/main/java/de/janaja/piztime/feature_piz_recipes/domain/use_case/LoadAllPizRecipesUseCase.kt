package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class LoadAllPizRecipesUseCase(
    private val repository: Repository
) {
    // should have only one public function!
    suspend operator fun invoke() {
        repository.loadAllPizRecipes()
    }
}