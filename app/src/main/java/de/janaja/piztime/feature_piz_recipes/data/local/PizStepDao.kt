package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

@Dao
interface PizStepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizStep(pizIngredient: PizStepEntity)

    // LEFT JOIN also gets PizSteps that have no associated PizStepIngredients
    // returns empty List for those PizStep entries
    @Query(
        "SELECT * FROM PizStepEntity " +
                "LEFT JOIN PizStepIngredientEntity ON PizStepEntity.stepid = PizStepIngredientEntity.stepIdMap " +
                "WHERE PizStepEntity.recipeIdMap = :id"
    )
    fun findPizStepsWithPizStepIngredientsByPizRecipeIdFlow(id: String): Map<PizStepEntity, List<PizStepIngredientEntity>>

    @Query(
        "SELECT * FROM PizStepEntity " +
                "LEFT JOIN PizStepIngredientEntity ON PizStepEntity.stepid = PizStepIngredientEntity.stepIdMap " +
                "WHERE PizStepEntity.recipeIdMap = :id"
    )
    suspend fun findPizStepsWithPizStepIngredientsByPizRecipeId(id: String): Map<PizStepEntity, List<PizStepIngredientEntity>>

    @Query(
        "SELECT * FROM PizStepEntity " +
                "WHERE PizStepEntity.recipeIdMap = :id"
    )
    suspend fun findPizStepsByPizRecipeId(id: String): List<PizStepEntity>

    @Query("SELECT * FROM PizStepEntity WHERE stepid = :id")
    suspend fun getStep(id: String): PizStepEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizSteps(pizStepEntities: List<PizStepEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPizStep(pizStepEntity: PizStepEntity)
    @Query("DELETE FROM PizStepEntity WHERE PizStepEntity.recipeIdMap = :recipeId")
    fun deletePizStepsForRecipeId(recipeId: String)

    @Query("DELETE FROM PizStepEntity WHERE stepid = :id")
    fun deletePizStep(id: String)

}