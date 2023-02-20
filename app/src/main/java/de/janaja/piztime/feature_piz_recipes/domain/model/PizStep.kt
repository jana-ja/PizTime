package de.janaja.piztime.feature_piz_recipes.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizStep(
    var description: String,
    @ColumnInfo(name="recipeIdMap")
    var recipeId: Long = 0,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="stepid")
    var id: Long = 0
) {

}