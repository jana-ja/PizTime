package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizStepEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateStepUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(stepWithoutIngredients: PizStepWithIngredients, recipeId: Long){

        val stepEntity = stepWithoutIngredients.toPizStepEntity(recipeId)
        repository.insertPizStep(stepEntity)
    }
}