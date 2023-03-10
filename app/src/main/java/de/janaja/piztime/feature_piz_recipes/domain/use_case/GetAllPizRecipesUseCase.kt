package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetAllPizRecipesUseCase(
    private val repository: Repository
) {
    // should have only one public function!
    suspend operator fun invoke() {
        repository.getAllPizRecipes()
    }
}