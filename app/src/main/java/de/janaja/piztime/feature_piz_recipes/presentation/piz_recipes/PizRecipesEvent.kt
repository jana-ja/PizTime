package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

sealed class PizRecipesEvent{
    data class ClickPizRecipe(val id: Long): PizRecipesEvent()
}
