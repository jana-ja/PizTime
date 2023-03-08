package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class GetIngredientsUseCase (
    private val repository: Repository
){
    suspend operator fun invoke(id: Long): List<PizIngredient> {
        val pizIngredientEntities = repository.findPizIngredientsByPizRecipeId(id)
        return pizIngredientEntities.map { ingredientEntity -> ingredientEntity.toPizIngredient() }
    }
}