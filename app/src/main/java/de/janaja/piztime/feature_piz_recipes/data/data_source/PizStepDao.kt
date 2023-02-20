package de.janaja.piztime.feature_piz_recipes.data.data_source

import androidx.room.*
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStep
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepIngredient
import kotlinx.coroutines.flow.Flow

@Dao
interface PizStepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPizStep(pizIngredient: PizStep): Long

    // LEFT JOIN also gets PizSteps that have no associated PizStepIngredients
    // returns empty List for those PizStep entries
    @Query(
        "SELECT * FROM PizStep " +
                "LEFT JOIN PizStepIngredient ON PizStep.stepid = PizStepIngredient.stepIdMap " +
                "WHERE PizStep.recipeIdMap = :id"
    )
    fun findPizStepsWithPizStepIngredientsByPizRecipeId(id: Long): Flow<Map<PizStep, List<PizStepIngredient>>>

    @Update
    suspend fun updatePizStep(pizStep: PizStep)

}