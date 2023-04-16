package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class DeleteRecipeUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(recipeId: String, imageName: String) {
        repository.deletePizRecipeWithDetails(recipeId)
        repository.deletePizRecipeImage(imageName)

    }
}