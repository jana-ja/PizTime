package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.janaja.piztime.R
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

    // TODO beim laden vllt schon so 3 blasse placeholder anzeigen

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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            state.pizRecipes.forEachIndexed() { index, pizRecipe ->
                item {
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
            item{
                Button(onClick = {viewModel.onEvent(PizRecipesEvent.NewRecipe)}) {
                    Text(text = "Rezept hinzufügen")
                }
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
                    painter = painterResource(id = R.drawable.baseline_logout_24),
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
                    painter = painterResource(id = R.drawable.baseline_login_24),
                    contentDescription = "login"
                )
            }
        }

    }
}


