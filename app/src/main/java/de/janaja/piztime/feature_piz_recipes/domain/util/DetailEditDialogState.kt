package de.janaja.piztime.feature_piz_recipes.domain.util

enum class EditDialog{None, Header, Ingredient, Step}

data class DetailEditDialogState(
    val editDialogState: EditDialog = EditDialog.None
)