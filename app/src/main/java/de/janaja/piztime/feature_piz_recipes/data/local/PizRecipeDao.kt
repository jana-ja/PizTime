package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PizRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllPizRecipes(pizRecipeEntities: List<PizRecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizRecipe(pizRecipeEntity: PizRecipeEntity)

    @Query("SELECT * FROM PizRecipeEntity WHERE recipeId = :id")
    fun findPizRecipeByIdFlow(id: Long): Flow<PizRecipeEntity?>

    @Query("SELECT * FROM PizRecipeEntity WHERE recipeId = :id")
    fun findPizRecipeById(id: Long): PizRecipeEntity?

    @Query("SELECT * FROM PizRecipeEntity")
    fun getAllPizRecipes(): List<PizRecipeEntity>

    @Update
    suspend fun updatePizRecipe(pizRecipeEntity: PizRecipeEntity)

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM PizRecipeEntity) THEN 0 ELSE 1 END")
    suspend fun isEmpty(): Boolean
}