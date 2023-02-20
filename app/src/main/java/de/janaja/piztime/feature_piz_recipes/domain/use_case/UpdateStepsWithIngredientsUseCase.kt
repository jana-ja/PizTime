package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.domain.model.PizStep
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepIngredient
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateStepsWithIngredientsUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(stepsWithIngredients: List<Pair<PizStep, List<PizStepIngredient>>>) {
        stepsWithIngredients.forEach {
            repository.updatePizStep(it.first)
            repository.updatePizStepIngredients(it.second)
        }
    }
}