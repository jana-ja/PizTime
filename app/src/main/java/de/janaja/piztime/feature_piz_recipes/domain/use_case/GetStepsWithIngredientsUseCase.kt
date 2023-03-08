package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.mapper.stepWithIngredientEntitiesToStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class GetStepsWithIngredientsUseCase (
    private val repository: Repository
){
    operator fun invoke(pizRecipeId: Long): List<PizStepWithIngredients> {
        val stepEntitities: Map<PizStepEntity, List<PizStepIngredientEntity>> = repository.findPizStepsWithIngredientsByPizRecipeId(pizRecipeId)
        return stepWithIngredientEntitiesToStepWithIngredients(stepEntitities)
    }
}