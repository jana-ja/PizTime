package de.janaja.piztime.feature_piz_recipes.data.data_source

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepIngredient

@Dao
interface PizStepIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizStepIngredient(pizIngredient: PizStepIngredient)

    @Update
    suspend fun updatePizStepIngredients(pizStepIngredients: List<PizStepIngredient>)
}