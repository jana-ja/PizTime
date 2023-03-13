package de.janaja.piztime.feature_piz_recipes.presentation.edit_recipe

sealed class EditRecipeEvent {
    object ClickSaveIngredients : EditRecipeEvent()
    data class ClickRemoveIngredient(val index: Int) : EditRecipeEvent()
    object ClickAddIngredient : EditRecipeEvent()
    data class NameChanged(val index: Int, val value: String): EditRecipeEvent()
    data class AmountChanged(val index: Int, val value: String): EditRecipeEvent()

    object ClickSaveSteps : EditRecipeEvent()
    data class ClickRemoveStep(val index: Int) : EditRecipeEvent()
    data class StepChanged(val index: Int, val value: String): EditRecipeEvent()
    object ClickAddStep : EditRecipeEvent()
    data class StepIngredientNameChanged(val stepIndex: Int, val index: Int, val value: String): EditRecipeEvent()
    data class StepIngredientAmountChanged(val stepIndex: Int, val index: Int, val value: String): EditRecipeEvent()


}
