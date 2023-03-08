package de.janaja.piztime.feature_piz_recipes.presentation.edit_recipe

sealed class EditRecipeEvent {
    object ClickSaveIngredients : EditRecipeEvent()
    data class ClickRemove(val index: Int) : EditRecipeEvent()
    object ClickAdd : EditRecipeEvent()
    data class NameChanged(val index: Int, val value: String): EditRecipeEvent()
    data class AmountChanged(val index: Int, val value: String): EditRecipeEvent()

    object ClickSaveSteps : EditRecipeEvent()
    data class StepChanged(val index: Int, val value: String): EditRecipeEvent()
}
