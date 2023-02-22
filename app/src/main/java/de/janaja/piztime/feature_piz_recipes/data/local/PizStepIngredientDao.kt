package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

@Dao
interface PizStepIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizStepIngredient(pizIngredient: PizStepIngredientEntity)

    @Update
    suspend fun updatePizStepIngredients(pizStepIngredientEntities: List<PizStepIngredientEntity>)
}