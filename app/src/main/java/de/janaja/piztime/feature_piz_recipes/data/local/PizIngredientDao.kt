package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PizIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizIngredient(pizIngredientEntity: PizIngredientEntity)

    // doesn't work when all objects need to have an id assigned
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addAllPizIngredients(pizIngredients: List<PizIngredient>)

    @Query("SELECT * FROM PizIngredientEntity WHERE recipeIdMap = :pizRecipeId")
    fun findPizIngredientsByPizRecipeIdFlow(pizRecipeId: Long): Flow<List<PizIngredientEntity>> // TODO does flow work on this kind of query???

    @Query("SELECT * FROM PizIngredientEntity WHERE recipeIdMap = :pizRecipeId")
    fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): List<PizIngredientEntity>

    @Update
    suspend fun updatePizIngredients(pizIngredientEntities: List<PizIngredientEntity>)
}