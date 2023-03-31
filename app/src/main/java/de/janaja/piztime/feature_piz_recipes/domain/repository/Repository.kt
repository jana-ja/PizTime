package de.janaja.piztime.feature_piz_recipes.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
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
    suspend fun getPizStep(id: Long): PizStepEntity
    suspend fun getPizRecipe(id: Long): PizRecipe?
    suspend fun getRecipeImage(imageName: String): ImageBitmap?
    suspend fun getPizIngredient(id: Long): PizIngredientEntity
    suspend fun getPizStepIngredient(id: Long): PizStepIngredientEntity

    // find by
    suspend fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): List<PizIngredientEntity>
    fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Map<PizStepEntity, List<PizStepIngredientEntity>>

    // delete
    suspend fun deletePizIngredientsForRecipeId(recipeId: Long)
    suspend fun deletePizStepsForRecipeId(recipeId: Long)
    suspend fun deletePizStepIngredientsForStepId(stepId: Long)
    suspend fun deletePizStepWithIngredients(id: Long)
    suspend fun deletePizStepIngredient(id: Long)
    suspend fun deletePizIngredient(id: Long)

    // insert
    suspend fun insertPizIngredients(pizIngredientEntities: List<PizIngredientEntity>)
    suspend fun insertPizIngredient(pizIngredientEntity: PizIngredientEntity)
    suspend fun insertPizStepIngredient(pizStepIngredientEntity: PizStepIngredientEntity)
    suspend fun insertPizSteps(pizStepEntities: List<PizStepEntity>)
    suspend fun insertPizStep(pizStepEntity: PizStepEntity)
    suspend fun insertPizStepIngredients(pizStepIngredientEntities: List<PizStepIngredientEntity>)

    // save
    suspend fun saveRecipeImage(urlOrWhatever: String, bitmap: ImageBitmap)

    // update
    suspend fun updatePizRecipe(pizRecipeEntity: PizRecipeEntity)
}