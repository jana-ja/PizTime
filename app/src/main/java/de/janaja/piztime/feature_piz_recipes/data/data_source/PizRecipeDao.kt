package de.janaja.piztime.feature_piz_recipes.data.data_source

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface PizRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllPizRecipes(pizRecipes: List<PizRecipe>)

    @Query("SELECT * FROM PizRecipe WHERE id = :id")
    fun findPizRecipeById(id: Long): Flow<PizRecipe?>

//    @Query("SELECT * FROM PizRecipe " +
//            "JOIN PizIngredient ON PizRecipe.id = PizIngredient.recipeId " +
//            "WHERE PizRecipe.id = :id")
//    suspend fun findPizRecipeWithIngredientsById(id: Long): Map<PizRecipe, List<PizIngredient>>?

    @Query("SELECT * FROM PizRecipe")
    fun getAllPizRecipes(): Flow<List<PizRecipe>>

    @Update
    suspend fun updatePizRecipe(pizRecipe: PizRecipe)

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM PizRecipe) THEN 0 ELSE 1 END")
    suspend fun isEmpty(): Boolean
}