package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

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
    fun findPizStepsWithPizStepIngredientsByPizRecipeIdFlow(id: Long): Map<PizStepEntity, List<PizStepIngredientEntity>>

    @Query(
        "SELECT * FROM PizStepEntity " +
                "LEFT JOIN PizStepIngredientEntity ON PizStepEntity.stepid = PizStepIngredientEntity.stepIdMap " +
                "WHERE PizStepEntity.recipeIdMap = :id"
    )
    fun findPizStepsWithPizStepIngredientsByPizRecipeId(id: Long): Map<PizStepEntity, List<PizStepIngredientEntity>>

    @Query("SELECT * FROM PizStepEntity WHERE stepid = :id")
    suspend fun getStep(id: Long): PizStepEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizSteps(pizStepEntities: List<PizStepEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizStep(pizStepEntity: PizStepEntity)
    @Query("DELETE FROM PizStepEntity WHERE PizStepEntity.recipeIdMap = :recipeId")
    fun deletePizStepsForRecipeId(recipeId: Long)

}