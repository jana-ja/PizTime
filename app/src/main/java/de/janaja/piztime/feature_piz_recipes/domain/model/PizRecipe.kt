package de.janaja.piztime.feature_piz_recipes.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizRecipe(
    var title: String,
    var feature: String,
    var imageResourceId: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="recipeId")
    var id: Long = 0
)