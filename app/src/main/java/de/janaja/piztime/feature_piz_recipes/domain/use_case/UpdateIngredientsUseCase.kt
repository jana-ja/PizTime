package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateIngredientsUseCase (
    private val repository: Repository
){
    suspend operator fun invoke(pizIngredientEntities: List<PizIngredientEntity>){
       repository.updatePizIngredients(pizIngredientEntities)
    }
}