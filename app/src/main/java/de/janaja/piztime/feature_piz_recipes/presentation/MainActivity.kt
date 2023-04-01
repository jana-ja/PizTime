package de.janaja.piztime.feature_piz_recipes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailScreen
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.PizRecipesScreen
import de.janaja.piztime.feature_piz_recipes.presentation.util.Screen
import de.janaja.piztime.ui.theme.PizTimeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.PizRecipesScreen.route
                    ) {
                        composable(route = Screen.PizRecipesScreen.route) {
                            PizRecipesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.PizRecipeDetailScreen.route + "pizRecipeId={pizRecipeId}",
                            arguments = listOf(
                                navArgument("pizRecipeId") {
                                    type = NavType.StringType
                                })
                        ) {
                            // TODO how to handle non optional arguments
                            it.arguments?.let { it1 -> PizRecipeDetailScreen(navController = navController) }
                        }
                    }
                }
            }
        }
    }
}



