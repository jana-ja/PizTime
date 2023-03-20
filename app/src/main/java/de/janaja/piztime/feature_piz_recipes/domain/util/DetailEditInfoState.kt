package de.janaja.piztime.feature_piz_recipes.domain.util

data class DetailEditInfoState (
    val title: String = "",
    val feature: String = "",
    val imageResId: Int = -1,
    val prepTime: String = "0.0"
)