package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

sealed class PizRecipesEvent{

    data class EmailChanged(val value: String): PizRecipesEvent()
    data class PasswordChanged(val value: String): PizRecipesEvent()

    object DismissDialog: PizRecipesEvent()
    object ShowDialog: PizRecipesEvent()

    object LogIn: PizRecipesEvent()
    object LogOut: PizRecipesEvent()

}
