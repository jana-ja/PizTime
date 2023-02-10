package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

// all possible events triggered from the ui
sealed class PizRecipeDetailEvent{
    data class SetAmount(val amount: Int): PizRecipeDetailEvent()
    // edit undso?
}
