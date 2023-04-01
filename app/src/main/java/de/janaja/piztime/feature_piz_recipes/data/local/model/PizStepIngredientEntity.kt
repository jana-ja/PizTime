package de.janaja.piztime.feature_piz_recipes.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PizStepIngredientEntity(
    var ingredient: String,
    var baseStepAmount: Double,
    @ColumnInfo(name="stepidMap")
    var stepId: String,
    @PrimaryKey()
    @ColumnInfo(name="stepIngredientId")
    var id: String
) {
}