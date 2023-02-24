package de.janaja.piztime.feature_piz_recipes.data.mapper

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients

// domain -> domain
fun PizRecipeWithDetails.toPizRecipe(): PizRecipe {
    return PizRecipe(title, feature, imageResourceId, id)
}


// domain -> data
fun PizRecipe.toRecipeEntity(): PizRecipeEntity {
    return PizRecipeEntity(title, feature, imageResourceId, id)
}

fun PizRecipeWithDetails.toEntityCollection(): EntityCollection {
    val recipeId = id
    val pizRecipeEntity: PizRecipeEntity = PizRecipeEntity(title, feature, imageResourceId, id)
    val pizIngredientEntities: List<PizIngredientEntity> = ingredients.map { ingredient ->
        PizIngredientEntity(
            ingredient.ingredient,
            ingredient.baseAmount,
            recipeId,
            ingredient.id
        )
    }
    val pizStepWithIngredientEntities: List<Pair<PizStepEntity, List<PizStepIngredientEntity>>> =
        steps.map { stepWithIngredient ->
            Pair(
                PizStepEntity(stepWithIngredient.description, stepWithIngredient.id),
                stepWithIngredient.ingredients.map { ingredient ->
                    PizStepIngredientEntity(
                        ingredient.ingredient,
                        ingredient.baseAmount,
                        stepWithIngredient.id,
                        ingredient.id
                    )
                })
        }

    return EntityCollection(
        pizRecipeEntity,
        pizIngredientEntities,
        pizStepWithIngredientEntities
    )
}


// data -> domain
fun PizRecipeEntity.toRecipe(): PizRecipe {
    return PizRecipe(title, feature, imageResourceId, id)
}

fun EntityCollection.toPizRecipeWithDetails(): PizRecipeWithDetails {
    val pizIngredients: List<PizIngredient> = pizIngredientEntities.map { ingredientEntity ->
        PizIngredient(
            ingredientEntity.ingredient,
            ingredientEntity.baseAmount,
            ingredientEntity.id
        )
    }
    val pizStepsWithIngredients: List<PizStepWithIngredients> =
        pizStepWithIngredientEntities.map { stepWithIngredientEntitites ->
            PizStepWithIngredients(
                stepWithIngredientEntitites.first.description,
                stepWithIngredientEntitites.second.map { stepIngredientEntity ->
                    PizIngredient(
                        stepIngredientEntity.ingredient,
                        stepIngredientEntity.baseStepAmount,
                        stepIngredientEntity.id
                    )
                },
                stepWithIngredientEntitites.first.id
            )
        }

    return PizRecipeWithDetails(
        pizRecipeEntity.title,
        pizRecipeEntity.feature,
        pizRecipeEntity.imageResourceId,
        pizIngredients,
        pizStepsWithIngredients
    )
}
