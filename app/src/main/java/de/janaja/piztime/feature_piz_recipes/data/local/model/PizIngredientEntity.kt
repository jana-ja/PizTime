package de.janaja.piztime.feature_piz_recipes.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PizIngredientEntity(
    var ingredient: String,
    var baseAmount: Double,
    @ColumnInfo(name="recipeIdMap")
    var recipeId: Long = 0,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ingredientId")
    var id: Long = 0
) {
}