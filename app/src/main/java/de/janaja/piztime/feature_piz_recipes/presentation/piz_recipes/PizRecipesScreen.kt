package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.components.LoginView
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.components.PizCard
import de.janaja.piztime.feature_piz_recipes.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PizRecipesScreen(
    navController: NavController,
    viewModel: PizRecipesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val dialogState = viewModel.loginDialogState.value
    val hasUser = viewModel.hasUser.value

    val context = LocalContext.current

    // with key1 = true this only gets executed once and not on recomposition!
    LaunchedEffect(key1 = true) {
        viewModel.loadPizRecipes()
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PizRecipesViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.massage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            state.pizRecipes.forEachIndexed() { index, pizRecipe ->
                PizCard(
                    pizRecipeEntity = pizRecipe,
                    recipeImage = state.recipeImages[index],
                    onClick = {
                        navController.navigate(Screen.PizRecipeDetailScreen.route + "pizRecipeId=${pizRecipe.id}")
                    },
                    index = index
                )
            }
        }

        // log in dialog
        if (dialogState.show) {
            Dialog(onDismissRequest = {
                viewModel.onEvent(PizRecipesEvent.DismissDialog)
            }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.8f),
                    shape = RoundedCornerShape(size = 10.dp)
                ) {
                    LoginView(logIn = { viewModel.onEvent(PizRecipesEvent.LogIn) })
                }
            }
        }

        // log in button
        val buttonModifier = Modifier
            .size(36.dp)
            .align(Alignment.TopEnd)
            .padding(end = 8.dp, top = 8.dp)

        if (hasUser){
            IconButton(
                onClick = {
                    viewModel.onEvent(PizRecipesEvent.LogOut)
                },
                modifier = buttonModifier
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack ,
                    contentDescription = "logout"
                )

            }
        } else {
            IconButton(
                onClick = {
                    viewModel.onEvent(PizRecipesEvent.ShowDialog)
                },
                modifier = buttonModifier
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "login"
                )

            }
        }

    }
}


