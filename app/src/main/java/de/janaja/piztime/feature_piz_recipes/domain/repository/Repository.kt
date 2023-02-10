package de.janaja.piztime.feature_piz_recipes.domain.repository

import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun addPizRecipe(pizRecipe: PizRecipe)

    suspend fun findPizRecipeById(id: Long): PizRecipe?

    fun getAllPizRecipes(): Flow<List<PizRecipe>>

    suspend fun updatePizRecipe(pizRecipe: PizRecipe)

    suspend fun initDbIfEmpty()
}