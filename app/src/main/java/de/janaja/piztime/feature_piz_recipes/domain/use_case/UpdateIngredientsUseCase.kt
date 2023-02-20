package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateIngredientsUseCase (
    private val repository: Repository
){
    suspend operator fun invoke(pizIngredients: List<PizIngredient>){
       repository.updatePizIngredients(pizIngredients)
    }
}