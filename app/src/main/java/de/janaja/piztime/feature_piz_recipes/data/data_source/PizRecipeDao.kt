package de.janaja.piztime.feature_piz_recipes.data.data_source

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface PizRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizRecipe(pizRecipe: PizRecipe)

    @Query("SELECT * FROM PizRecipe WHERE id = :id")
    suspend fun findPizRecipeById(id: Long): PizRecipe?

    @Query("SELECT * FROM PizRecipe")
    fun getAllPizRecipes(): Flow<List<PizRecipe>>

    @Update
    suspend fun updatePizRecipeDetails(pizRecipe: PizRecipe)

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM PizRecipe) THEN 0 ELSE 1 END")
    suspend fun isEmpty(): Boolean
}