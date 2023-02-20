package de.janaja.piztime.feature_piz_recipes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizStep(
    var description: String,
    var recipeId: Long = 0,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) {

}