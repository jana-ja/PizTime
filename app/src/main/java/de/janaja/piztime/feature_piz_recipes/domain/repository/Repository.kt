package de.janaja.piztime.feature_piz_recipes.domain.repository

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun findPizRecipebyId(id: Long): Flow<PizRecipeEntity?>

    fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): Flow<List<PizIngredientEntity>>

    fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Flow<Map<PizStepEntity, List<PizStepIngredientEntity>>>

    fun getAllPizRecipes(): Flow<List<PizRecipeEntity>>

    suspend fun updatePizRecipe(pizRecipeEntity: PizRecipeEntity)
    
    suspend fun updatePizIngredients(pizIngredientEntities: List<PizIngredientEntity>)
    
    suspend fun updatePizStep(pizStepEntity: PizStepEntity)

    suspend fun updatePizStepIngredients(pizStepIngredientEntities: List<PizStepIngredientEntity>)

    suspend fun initDbIfEmpty()
}