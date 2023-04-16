package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.domain.util.DeleteDialog


// all possible events triggered from the ui
sealed class PizRecipeDetailEvent{

    object LaunchAnimation: PizRecipeDetailEvent()
    object IncreaseAmount: PizRecipeDetailEvent()
    object DecreaseAmount: PizRecipeDetailEvent()

    object ToggleEditMode: PizRecipeDetailEvent()

    object ClickEditInfo: PizRecipeDetailEvent()
    /**
     * Can be used for Ingredient (param stepId = null) and StepIngredient (param stepId = id_of_step).
     */
    data class ClickEditIngredient(val id: String, val isStepIngredient: Boolean = false, val stepId: String?): PizRecipeDetailEvent()
    object ClickEditImage: PizRecipeDetailEvent()
    data class ClickEditStep(val id: String): PizRecipeDetailEvent()

    object ClickSaveInfo : PizRecipeDetailEvent()
    object ClickSaveIngredient : PizRecipeDetailEvent()
    object ClickSaveImage: PizRecipeDetailEvent()
    object ClickSaveStep : PizRecipeDetailEvent()

    data class RecipeTitleChanged(val value: String): PizRecipeDetailEvent()
    data class RecipeFeatureChanged(val value: String): PizRecipeDetailEvent()
    data class RecipePrepTimeChanged(val value: String): PizRecipeDetailEvent()
    data class IngredientNameChanged(val value: String): PizRecipeDetailEvent()
    data class ImageChanged(val bitmap: ImageBitmap): PizRecipeDetailEvent()
    data class IngredientAmountChanged(val value: String): PizRecipeDetailEvent()
    data class StepDescriptionChanged(val value: String): PizRecipeDetailEvent()

    /**
     * Can be used for Ingredient (param stepId = null) and StepIngredient (param stepId = id_of_step).
     */
    data class ClickAddIngredient(val isStepIngredient: Boolean = false, val stepId: String?): PizRecipeDetailEvent()
    object ClickAddStep: PizRecipeDetailEvent()

    object ClickDeleteRecipe: PizRecipeDetailEvent()
    object ClickDeleteIngredient: PizRecipeDetailEvent()
    object ClickDeleteStep: PizRecipeDetailEvent()

    object DismissEditDialog: PizRecipeDetailEvent()

    // alert dialog
    data class ShowDeleteDialog(val deleteDialog: DeleteDialog): PizRecipeDetailEvent()
    object DismissDeleteDialog: PizRecipeDetailEvent()



}
