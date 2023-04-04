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
    val pizRecipeWithDetailsFlow: StateFlow<PizRecipeWithDetails?>
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
    suspend fun loadPizRecipeWithDetails(id: String)
    fun resetRecipeWithDetailsFlow()

    // get
    suspend fun getPizRecipe(id: String): PizRecipe?
    suspend fun getRecipeImage(imageName: String): ImageBitmap?
    suspend fun getPizIngredient(id: String, recipeId: String): PizIngredient?
    suspend fun getPizStepWithoutIngredients(id: String, recipeId: String): PizStepWithIngredients?
    suspend fun getPizStepIngredient(id: String, recipeId: String, stepId: String): PizIngredient?

    // delete
    suspend fun deletePizIngredientsForRecipeId(recipeId: String)
    suspend fun deletePizStepIngredientsForStepId(stepId: String, recipeId: String)
    suspend fun deletePizStepWithIngredients(id: String, recipeId: String)
    suspend fun deletePizStepIngredient(id: String, recipeId: String, stepId: String)
    suspend fun deletePizIngredient(id: String, recipeId: String)

    // insert
    suspend fun insertPizIngredients(pizIngredients: List<PizIngredient>, recipeId: String)
    suspend fun insertPizIngredient(pizIngredient: PizIngredient, recipeId: String)
    suspend fun insertPizStepIngredient(pizStepIngredient: PizIngredient, recipeId: String, stepId: String)
    suspend fun insertPizSteps(pizSteps: List<PizStepWithIngredients>, recipeId: String)
    suspend fun insertPizStep(pizStep: PizStepWithIngredients, recipeId: String)
    suspend fun insertPizStepIngredients(pizStepIngredients: List<PizIngredient>, recipeId: String, stepId: String)

    // save
    suspend fun saveRecipeImage(imageName: String, bitmap: ImageBitmap)

    // update
    suspend fun updatePizRecipe(pizRecipe: PizRecipe)
}