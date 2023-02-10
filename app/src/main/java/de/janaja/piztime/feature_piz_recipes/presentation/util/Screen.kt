package de.janaja.piztime.feature_piz_recipes.presentation.util

sealed class Screen(val route: String){
    object PizRecipesScreen: Screen("piz_overview_screen")
    object PizRecipeDetailScreen: Screen("piz_detail_screen")
}
