package de.janaja.piztime.feature_piz_recipes.domain.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizRecipe(
    var title: String,
    var feature: String,
    //var ingredients: List<Pair<String, Double>>,
    var description: String,
    var imageResourceId: Int,
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Long = 0
)