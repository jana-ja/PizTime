package de.janaja.piztime.feature_piz_recipes.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStep
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepIngredient

@Database(entities = [PizRecipe::class, PizIngredient::class, PizStep::class, PizStepIngredient::class], version = 1, exportSchema = false)
abstract class PizRecipeDatabase : RoomDatabase() {

    abstract val pizRecipeDao: PizRecipeDao
    abstract val pizIngredientDao: PizIngredientDao
    abstract val pizStepDao: PizStepDao
    abstract val pizStepIngredientDao: PizStepIngredientDao

    companion object {
        const val DATABASE_NAME = "piz_db"
    }
}