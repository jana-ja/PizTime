package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class GetPizRecipeUseCase(
    private val repository: Repository
){
    suspend operator fun invoke(id: Long): Pair<PizRecipe, List<PizIngredient>>?{
        return repository.findPizRecipeWithIngredientsById(id)
    }
}