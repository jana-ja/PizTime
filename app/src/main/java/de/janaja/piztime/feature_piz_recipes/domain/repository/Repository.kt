package de.janaja.piztime.feature_piz_recipes.domain.repository

import de.janaja.piztime.feature_piz_recipes.domain.model.*
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun findPizRecipebyId(id: Long): Flow<PizRecipe?>

    fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): Flow<List<PizIngredient>>

    fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Flow<Map<PizStep, List<PizStepIngredient>>>

    fun getAllPizRecipes(): Flow<List<PizRecipe>>

    suspend fun updatePizRecipe(pizRecipe: PizRecipe)
    
    suspend fun updatePizIngredients(pizIngredients: List<PizIngredient>)
    
    suspend fun updatePizStep(pizStep: PizStep)

    suspend fun updatePizStepIngredients(pizStepIngredients: List<PizStepIngredient>)

    suspend fun initDbIfEmpty()
}