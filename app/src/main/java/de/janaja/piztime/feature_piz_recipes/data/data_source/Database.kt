package de.janaja.piztime.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizRecipeDao
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

@Database(entities = [(PizRecipe::class)], version = 1, exportSchema = false)
abstract class PizRecipeDatabase : RoomDatabase() {

    abstract val pizRecipeDao: PizRecipeDao

    companion object {
        const val DATABASE_NAME = "piz_db"
    }
}