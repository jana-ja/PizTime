package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

// all possible events triggered from the ui
sealed class PizRecipeDetailEvent{
    object IncreaseAmount: PizRecipeDetailEvent()
    object DecreaseAmount: PizRecipeDetailEvent()
    object ClickEditHeader: PizRecipeDetailEvent()
    object ClickEditIngredients: PizRecipeDetailEvent()
    object ClickEditSteps: PizRecipeDetailEvent()
    object DismissDialog: PizRecipeDetailEvent()
}
