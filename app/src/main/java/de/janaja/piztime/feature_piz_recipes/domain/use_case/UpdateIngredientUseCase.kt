package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateIngredientUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(pizIngredient: PizIngredient, isStepIngredient: Boolean, mapId: Long) {
        if(isStepIngredient){
            repository.insertPizStepIngredient(pizIngredient.toPizStepIngredientEntity(mapId))
        } else {
            repository.insertPizIngredient(pizIngredient.toPizIngredientEntity(mapId))
        }
    }
}