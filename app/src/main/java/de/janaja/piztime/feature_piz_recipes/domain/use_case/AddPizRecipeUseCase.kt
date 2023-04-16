package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class AddPizRecipeUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(): String {
        val newRecipe = PizRecipe("Pizza Name","Special Feature","", 0.0)
        repository.insertPizRecipe(newRecipe)
        return newRecipe.id
    }
}