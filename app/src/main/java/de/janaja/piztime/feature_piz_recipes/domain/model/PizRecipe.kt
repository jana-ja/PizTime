package de.janaja.piztime.feature_piz_recipes.domain.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizRecipe(
    var title: String,
    var feature: String,
    var description: String,
    var imageResourceId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)