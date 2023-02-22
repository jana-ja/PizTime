package de.janaja.piztime.feature_piz_recipes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

@Database(entities = [PizRecipeEntity::class, PizIngredientEntity::class, PizStepEntity::class, PizStepIngredientEntity::class], version = 1, exportSchema = false)
abstract class PizRecipeDatabase : RoomDatabase() {

    abstract val pizRecipeDao: PizRecipeDao
    abstract val pizIngredientDao: PizIngredientDao
    abstract val pizStepDao: PizStepDao
    abstract val pizStepIngredientDao: PizStepIngredientDao

    companion object {
        const val DATABASE_NAME = "piz_db"
    }
}