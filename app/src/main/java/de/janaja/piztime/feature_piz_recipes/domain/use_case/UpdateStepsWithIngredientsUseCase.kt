package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateStepsWithIngredientsUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(stepsWithIngredients: List<Pair<PizStepEntity, List<PizStepIngredientEntity>>>) {
        stepsWithIngredients.forEach {
            repository.updatePizStep(it.first)
            repository.updatePizStepIngredients(it.second)
        }
    }
}