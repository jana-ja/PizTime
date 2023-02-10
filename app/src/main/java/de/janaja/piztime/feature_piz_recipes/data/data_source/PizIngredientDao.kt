package de.janaja.piztime.feature_piz_recipes.data.data_source

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface PizIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizIngredient(pizIngredient: PizIngredient)

    // doesnt work when all objects need to have an id assigned
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addAllPizIngredient(pizIngredients: List<PizIngredient>)

    @Query("SELECT * FROM PizIngredient WHERE id = :id")
    suspend fun findPizIngredientById(id: Long): PizIngredient?

    @Query("SELECT * FROM PizIngredient")
    fun getAllPizIngredients(): Flow<List<PizIngredient>>

    @Update
    suspend fun updatePizIngredientDetails(pizIngredient: PizIngredient)

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM PizIngredient) THEN 0 ELSE 1 END")
    suspend fun isEmpty(): Boolean
}