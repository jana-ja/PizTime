package de.janaja.piztime.feature_piz_recipes.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizStepEntity(
    var description: String,
    @ColumnInfo(name="recipeIdMap")
    var recipeId: String,
    @PrimaryKey()
    @ColumnInfo(name="stepid")
    var id: String
) {

}