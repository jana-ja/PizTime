package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity

@Dao
interface PizIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizIngredient(pizIngredientEntity: PizIngredientEntity)

    // doesn't work when all objects need to have an id assigned
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addAllPizIngredients(pizIngredients: List<PizIngredient>)

    @Query("SELECT * FROM PizIngredientEntity WHERE recipeIdMap = :pizRecipeId")
    suspend fun findPizIngredientsByPizRecipeIdFlow(pizRecipeId: Long): List<PizIngredientEntity> // TODO does flow work on this kind of query???

    @Query("SELECT * FROM PizIngredientEntity WHERE recipeIdMap = :pizRecipeId")
    fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): List<PizIngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizIngredients(pizIngredientEntities: List<PizIngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizIngredient(pizIngredientEntitiy: PizIngredientEntity)

    @Query("SELECT * FROM PizIngredientEntity WHERE ingredientId = :id")
    suspend fun getPizIngredient(id: Long): PizIngredientEntity

    @Query("DELETE FROM PizIngredientEntity WHERE recipeIdMap = :recipeId")
    suspend fun deletePizIngredientsForRecipeId(recipeId: Long)
}