package de.janaja.piztime.feature_piz_recipes.data.data_source

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import kotlinx.coroutines.flow.Flow

@Dao
interface PizIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizIngredient(pizIngredient: PizIngredient)

    // doesn't work when all objects need to have an id assigned
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addAllPizIngredients(pizIngredients: List<PizIngredient>)

    @Query("SELECT * FROM PizIngredient WHERE recipeId = :pizRecipeId")
    fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): Flow<List<PizIngredient>> // TODO does flow work on this kind of query???

    @Update
    suspend fun updatePizIngredients(pizIngredients: List<PizIngredient>)
}