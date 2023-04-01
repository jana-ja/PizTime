package de.janaja.piztime.feature_piz_recipes.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    // using flow so i can trigger data updates from multiple sources, which will get observed
    // f.e. reloading data after saving change to backend from edit dialog
    val pizRecipeWithDetailsFlow: StateFlow<PizRecipeWithDetails>
    val allPizRecipesFlow: StateFlow<List<PizRecipe>>

    suspend fun initDbIfEmpty()

    // load to flows
    /**
     * Try to load all recipes from db and update into Flow member.
     * Observe the Flow variable to retrieve the data.
     */
    suspend fun loadAllPizRecipes()
    /**
     * Try to load Recipe With Ingredients and Steps from db and update into Flow member.
     * Observe the Flow variable to retrieve the data.
     */
    suspend fun loadPizRecipeWithDetails(id: Long)

    // get
    suspend fun getPizRecipe(id: Long): PizRecipe?
    suspend fun getRecipeImage(imageName: String): ImageBitmap?
    suspend fun getPizIngredient(id: Long): PizIngredient
    suspend fun getPizStepWithoutIngredients(id: Long): PizStepWithIngredients
    suspend fun getPizStepIngredient(id: Long): PizIngredient

//    // find by
//    suspend fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): List<PizIngredient>
//    fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): PizStepWithIngredients

    // delete
    suspend fun deletePizIngredientsForRecipeId(recipeId: Long)
    suspend fun deletePizStepsForRecipeId(recipeId: Long)
    suspend fun deletePizStepIngredientsForStepId(stepId: Long)
    suspend fun deletePizStepWithIngredients(id: Long)
    suspend fun deletePizStepIngredient(id: Long)
    suspend fun deletePizIngredient(id: Long)

    // insert
    suspend fun insertPizIngredients(pizIngredients: List<PizIngredient>, recipeId: Long)
    suspend fun insertPizIngredient(pizIngredient: PizIngredient, mapId: Long)
    suspend fun insertPizStepIngredient(pizStepIngredient: PizIngredient, mapId: Long)
    suspend fun insertPizSteps(pizSteps: List<PizStepWithIngredients>, recipeId: Long)
    suspend fun insertPizStep(pizStep: PizStepWithIngredients, recipeId: Long)
    suspend fun insertPizStepIngredients(pizStepIngredients: List<PizIngredient>, stepId: Long)

    // save
    suspend fun saveRecipeImage(urlOrWhatever: String, bitmap: ImageBitmap)

    // update
    suspend fun updatePizRecipe(pizRecipe: PizRecipe)
}