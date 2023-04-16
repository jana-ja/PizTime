package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

@Dao
interface PizStepIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizStepIngredient(pizIngredient: PizStepIngredientEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizStepIngredients(pizStepIngredientEntities: List<PizStepIngredientEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizStepIngredient(pizStepIngredientEntity: PizStepIngredientEntity)
    @Query("SELECT * FROM PizStepIngredientEntity WHERE stepIngredientId = :id")
    suspend fun getPizStepIngredient(id: String): PizStepIngredientEntity
    @Query("DELETE FROM PizStepIngredientEntity WHERE stepIngredientId = :id")
    fun deletePizStepIngredient(id: String)
    @Query("DELETE FROM PizStepIngredientEntity WHERE stepidMap = :stepId")
    fun deletePizStepIngredientsForStepId(stepId: String)

}