package de.janaja.piztime.feature_piz_recipes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PizStepIngredient(
    var ingredient: String,
    var baseStepAmount: Double,
    var stepId: Long = 0,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) {
}