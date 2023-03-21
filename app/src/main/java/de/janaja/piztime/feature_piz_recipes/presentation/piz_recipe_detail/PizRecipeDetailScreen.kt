package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.util.EditDialog
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.*
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PizRecipeDetailScreen(
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    val amountState = viewModel.detailAmountState.value
    val recipeState = viewModel.pizRecipeState.value
    val dialogState = viewModel.detailEditDialogState.value
    val editState = viewModel.detailEditModeState.value

    val context = LocalContext.current

    PizRecipeDetailView(
        modifier = Modifier,//.offset(x = offset.dp),
        recipeState.pizRecipe,
        recipeState.firstLaunch,
        amountState.amount,
        dialogState.editDialogState,
        editState.editMode,
        viewModel::onEvent
    )

    // with key1 = true this only gets executed once and not on recomposition!
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PizRecipeDetailViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.massage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizRecipeDetailView(
    modifier: Modifier,
    pizRecipeWithDetails: PizRecipeWithDetails,
    firstLaunch: Boolean,
    amount: Int,
    dialogState: EditDialog,
    editMode: Boolean,
    onEvent: (PizRecipeDetailEvent) -> Unit
) {

    val overlap: Dp = dimensionResource(id = R.dimen.topSheetBorderHeight)
    val headerHeight = 150.dp
    val cardModifier = Modifier.background(MaterialTheme.colorScheme.surface)

    Box {
        // edit dialog
        if (dialogState != EditDialog.None) {
            Dialog(onDismissRequest = {
                onEvent(PizRecipeDetailEvent.DismissDialog)
            }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.8f),
                    shape = RoundedCornerShape(size = 10.dp)
                ) {
                    when (dialogState) {
                        EditDialog.Header -> {
                            EditHeaderView()
                        }
                        EditDialog.Ingredient -> {
                            EditIngredientView()
                        }
                        EditDialog.Step -> {
                            EditStepView()
                        }
                        EditDialog.None -> {}
                    }

                }
            }
        }


        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(-overlap)

        ) {

            // if sections should have different background colors or have no gaps in general,
            // then they have to overlap (negative space in layout arrangement and extra padding for the views tops)
            item {
                HeaderView(
                    modifier = cardModifier.zIndex(1f),
                    recipe = pizRecipeWithDetails.toPizRecipe(),
                    contentModifier = Modifier,
                    height = headerHeight,
                    onEvent = onEvent,
                    editMode = editMode,
                    firstLaunch = firstLaunch
                )
            }
            item {
                IngredientsView(
                    modifier = cardModifier.zIndex(.9f),
                    ingredients = pizRecipeWithDetails.ingredients,
                    amount = amount,
                    onEvent = onEvent,
                    contentModifier = Modifier.padding(top = overlap),
                    editMode = editMode
                )
            }
            item {
                StepsView(
                    modifier = cardModifier.zIndex(.8f),
                    stepsWithIngredients = pizRecipeWithDetails.steps,
                    amount = amount,
                    contentModifier = Modifier.padding(top = overlap),
                    onEvent = onEvent,
                    editMode = editMode
                )

            }
        }

        // edit button
        IconButton(
            onClick = { onEvent(PizRecipeDetailEvent.ClickEdit) },
            Modifier
                .size(36.dp)
                .align(Alignment.TopEnd)
                .padding(end = 8.dp, top = 8.dp)
        ) {
            Icon(
                Icons.Default.Edit,
                "edit recipe",
                Modifier.fillMaxHeight()
            )

        }
    }
}


@Preview
@Composable
fun PizRecipeDetailViewPreview() {
    PizRecipeDetailView(
        modifier = Modifier.background(Color(0xFFC49E2F)),
        pizRecipeWithDetails = DummyData.DummyPizRecipeWithDetails,
        true,
        amount = 4,
        dialogState = EditDialog.None,
        editMode = false
    ) { }
}


