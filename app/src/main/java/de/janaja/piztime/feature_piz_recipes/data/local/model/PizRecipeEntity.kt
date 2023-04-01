package de.janaja.piztime.feature_piz_recipes.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizRecipeEntity(
    var title: String,
    var feature: String,
    var imageName: String,
    var prepTime: Double,
    @PrimaryKey()
    @ColumnInfo(name="recipeId")
    var id: String
)