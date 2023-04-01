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
    return PizRecipe(title, feature, imageName, prepTime, id)
}


// domain -> data
fun PizRecipe.toRecipeEntity(): PizRecipeEntity {
    return PizRecipeEntity(title, feature, imageName, prepTime, id)
}

fun PizRecipeWithDetails.toEntityCollection(): EntityCollection {
    val recipeId = id
    val pizRecipeEntity = PizRecipeEntity(title, feature, imageName, prepTime, id)
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
                PizStepEntity(stepWithIngredient.description, recipeId),
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

fun PizIngredient.toPizIngredientEntity(recipeId: Long): PizIngredientEntity {
    return PizIngredientEntity(this.ingredient, this.baseAmount, recipeId, this.id)
}

fun PizIngredient.toPizStepIngredientEntity(stepId: Long): PizStepIngredientEntity {
    return PizStepIngredientEntity(this.ingredient, this.baseAmount, stepId, this.id)
}

fun PizStepWithIngredients.toPizStepEntity(recipeId: Long): PizStepEntity {
    return PizStepEntity(this.description, recipeId, this.id)
}

// data -> domain
fun PizRecipeEntity.toRecipe(): PizRecipe {
    return PizRecipe(title, feature, imageName, prepTime, id)
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
        pizRecipeEntity.imageName,
        pizRecipeEntity.prepTime,
        pizIngredients,
        pizStepsWithIngredients,
        pizRecipeEntity.id
    )
}

fun PizIngredientEntity.toPizIngredient(): PizIngredient {
    return PizIngredient(this.ingredient, this.baseAmount, this.id)
}

fun PizStepIngredientEntity.toPizIngredient(): PizIngredient {
    return PizIngredient(this.ingredient, this.baseStepAmount, this.id)
}

fun PizStepEntity.toPizStepWithoutIngredients() : PizStepWithIngredients{
    return PizStepWithIngredients(this.description, listOf(), this.id)
}

fun stepWithIngredientEntitiesToStepWithIngredients(entities: Map<PizStepEntity, List<PizStepIngredientEntity>>): List<PizStepWithIngredients> {

    return entities.map { entry ->

        PizStepWithIngredients(
            description = entry.key.description,
            ingredients = entry.value.map { ingredientEntitiy ->
                PizIngredient(
                    ingredientEntitiy.ingredient,
                    ingredientEntitiy.baseStepAmount,
                    ingredientEntitiy.id
                )
            },
            id = entry.key.id
        )
    }
}
