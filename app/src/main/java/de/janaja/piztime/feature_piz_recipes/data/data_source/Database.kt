package de.janaja.piztime.feature_piz_recipes.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizIngredientDao
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizRecipeDao
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

@Database(entities = [PizRecipe::class, PizIngredient::class], version = 1, exportSchema = false)
abstract class PizRecipeDatabase : RoomDatabase() {

    abstract val pizRecipeDao: PizRecipeDao
    abstract val pizIngredientDao: PizIngredientDao

    companion object {
        const val DATABASE_NAME = "piz_db"
    }
}