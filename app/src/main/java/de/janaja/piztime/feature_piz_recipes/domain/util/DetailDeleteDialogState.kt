package de.janaja.piztime.feature_piz_recipes.domain.util

enum class DeleteDialog{None, Recipe, Ingredient, Step}

data class DetailDeleteDialogState(
    val deleteDialog: DeleteDialog = DeleteDialog.None
)