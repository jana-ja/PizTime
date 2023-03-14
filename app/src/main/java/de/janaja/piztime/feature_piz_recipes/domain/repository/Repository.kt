package de.janaja.piztime.feature_piz_recipes.domain.repository

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    val pizRecipeWithDetailsFlow: StateFlow<PizRecipeWithDetails>
    val allPizRecipesFlow: StateFlow<List<PizRecipe>>

    fun findPizRecipeById(id: Long): Flow<PizRecipeEntity?>

    suspend fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): List<PizIngredientEntity>

    fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Map<PizStepEntity, List<PizStepIngredientEntity>>

    suspend fun getPizStep(id: Long): PizStepEntity
    suspend fun getAllPizRecipes()

    suspend fun updatePizRecipe(pizRecipeEntity: PizRecipeEntity)

    suspend fun insertPizIngredients(pizIngredientEntities: List<PizIngredientEntity>)
    suspend fun insertPizIngredient(pizIngredientEntity: PizIngredientEntity)
    suspend fun insertPizStepIngredient(pizStepIngredientEntity: PizStepIngredientEntity)

    suspend fun deletePizIngredientsForRecipeId(recipeId: Long)
    suspend fun deletePizStepsForRecipeId(recipeId: Long)
    suspend fun deletePizStepIngredientsForStepId(stepId: Long)

    suspend fun insertPizSteps(pizStepEntities: List<PizStepEntity>)
    suspend fun insertPizStep(pizStepEntity: PizStepEntity)

    suspend fun insertPizStepIngredients(pizStepIngredientEntities: List<PizStepIngredientEntity>)

    suspend fun initDbIfEmpty()

    suspend fun findPizRecipeWithDetailsById(id: Long)

    suspend fun getPizIngredient(id: Long): PizIngredientEntity
    suspend fun getPizStepIngredient(id: Long): PizStepIngredientEntity
}