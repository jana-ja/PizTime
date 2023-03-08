package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdateStepsWithIngredientsUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(stepsWithIngredients: List<PizStepWithIngredients>, recipeId: Long){//List<Pair<PizStepEntity, List<PizStepIngredientEntity>>>) {
        // TODO maybe write something for this in wrapper
        // seperate to steps and stepingredients
        val stepsEntities = stepsWithIngredients.map { stepWithIngredients -> PizStepEntity(stepWithIngredients.description, recipeId, stepWithIngredients.id) }
        val stepIngredients = stepsWithIngredients.map { stepWithIngredients -> stepWithIngredients.ingredients.map {
                ingredient -> PizStepIngredientEntity(ingredient.ingredient, ingredient.baseAmount, stepWithIngredients.id, ingredient.id)
        } }
        // delete and insert steps
        repository.deletePizStepsForRecipeId(recipeId)
        repository.insertPizSteps(stepsEntities)
        // delete and insert ingredients
        stepIngredients.forEach {
            if(it.isNotEmpty()) {
                val stepId = it.first().stepId
                repository.deletePizStepIngredientsForStepId(stepId)
                repository.insertPizStepIngredients(it)
            }
        }
    }
}