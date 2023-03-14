package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail


// all possible events triggered from the ui
sealed class PizRecipeDetailEvent{
    object IncreaseAmount: PizRecipeDetailEvent()
    object DecreaseAmount: PizRecipeDetailEvent()
    object ClickEditHeader: PizRecipeDetailEvent()
    data class ClickEditIngredient(val id: Long, val isStepIngredient: Boolean = false, val stepId: Long = -1): PizRecipeDetailEvent()
    data class ClickEditStep(val id: Long): PizRecipeDetailEvent()
    object DismissDialog: PizRecipeDetailEvent()
    object ClickEdit: PizRecipeDetailEvent()


    object ClickSaveIngredient : PizRecipeDetailEvent()
    object ClickSaveStep : PizRecipeDetailEvent()
    data class IngredientNameChanged(val value: String): PizRecipeDetailEvent()
    data class IngredientAmountChanged(val value: String): PizRecipeDetailEvent()
    data class StepDescriptionChanged(val value: String): PizRecipeDetailEvent()
    data class ClickAddIngredient(val isStepIngredient: Boolean = false, val stepId: Long = -1): PizRecipeDetailEvent()
    object ClickAddStep: PizRecipeDetailEvent()



}
