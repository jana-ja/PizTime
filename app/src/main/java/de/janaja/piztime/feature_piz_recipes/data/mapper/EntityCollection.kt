package de.janaja.piztime.feature_piz_recipes.data.mapper

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

// pizrecipedto, list pizingredientsdto, list pair (pizstepdto und list pizstepingredient)

data class EntityCollection(
    var pizRecipeEntity: PizRecipeEntity,
    var pizIngredientEntities: List<PizIngredientEntity>,
    var pizStepWithIngredientEntities: List<Pair<PizStepEntity, List<PizStepIngredientEntity>>>
)
