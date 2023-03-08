package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateIngredientsUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(pizIngredients: List<PizIngredient>, recipeId: Long) {
        // delete old
        repository.deletePizIngredientsForRecipeId(recipeId)
        // insert new
        val pizIngredientEntities = pizIngredients.map { ingredient -> ingredient.toPizIngredientEntity(recipeId) }
        repository.insertPizIngredients(pizIngredientEntities)
    }
}