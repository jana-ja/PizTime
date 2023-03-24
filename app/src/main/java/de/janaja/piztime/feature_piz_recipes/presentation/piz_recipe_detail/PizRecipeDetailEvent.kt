package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.ui.graphics.ImageBitmap


// all possible events triggered from the ui
sealed class PizRecipeDetailEvent{

    object LaunchAnimation: PizRecipeDetailEvent()
    object IncreaseAmount: PizRecipeDetailEvent()
    object DecreaseAmount: PizRecipeDetailEvent()

    object ToggleEditMode: PizRecipeDetailEvent()

    object ClickEditInfo: PizRecipeDetailEvent()
    data class ClickEditIngredient(val id: Long, val isStepIngredient: Boolean = false, val stepId: Long = -1): PizRecipeDetailEvent()
    object ClickEditImage: PizRecipeDetailEvent()
    data class ClickEditStep(val id: Long): PizRecipeDetailEvent()

    object ClickSaveInfo : PizRecipeDetailEvent()
    object ClickSaveIngredient : PizRecipeDetailEvent()
    object ClickSaveImage: PizRecipeDetailEvent()
    object ClickSaveStep : PizRecipeDetailEvent()

    data class RecipeTitleChanged(val value: String): PizRecipeDetailEvent()
    data class RecipeFeatureChanged(val value: String): PizRecipeDetailEvent()
    data class RecipePrepTimeChanged(val value: String): PizRecipeDetailEvent()
    data class IngredientNameChanged(val value: String): PizRecipeDetailEvent()
    data class ImageChanged(val bitmap: ImageBitmap, val urlOrWhatever: String): PizRecipeDetailEvent()
    data class IngredientAmountChanged(val value: String): PizRecipeDetailEvent()
    data class StepDescriptionChanged(val value: String): PizRecipeDetailEvent()

    data class ClickAddIngredient(val isStepIngredient: Boolean = false, val stepId: Long = -1): PizRecipeDetailEvent()
    object ClickAddStep: PizRecipeDetailEvent()

    object ClickDeleteIngredient: PizRecipeDetailEvent()
    object ClickDeleteStep: PizRecipeDetailEvent()

    object DismissDialog: PizRecipeDetailEvent()



}
