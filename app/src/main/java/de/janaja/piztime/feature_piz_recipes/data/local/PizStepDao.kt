package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PizStepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizStep(pizIngredient: PizStepEntity): Long

    // LEFT JOIN also gets PizSteps that have no associated PizStepIngredients
    // returns empty List for those PizStep entries
    @Query(
        "SELECT * FROM PizStepEntity " +
                "LEFT JOIN PizStepIngredientEntity ON PizStepEntity.stepid = PizStepIngredientEntity.stepIdMap " +
                "WHERE PizStepEntity.recipeIdMap = :id"
    )
    fun findPizStepsWithPizStepIngredientsByPizRecipeIdFlow(id: Long): Flow<Map<PizStepEntity, List<PizStepIngredientEntity>>>

    @Query(
        "SELECT * FROM PizStepEntity " +
                "LEFT JOIN PizStepIngredientEntity ON PizStepEntity.stepid = PizStepIngredientEntity.stepIdMap " +
                "WHERE PizStepEntity.recipeIdMap = :id"
    )
    fun findPizStepsWithPizStepIngredientsByPizRecipeId(id: Long): Map<PizStepEntity, List<PizStepIngredientEntity>>

    @Update
    suspend fun updatePizStep(pizStepEntity: PizStepEntity)

}