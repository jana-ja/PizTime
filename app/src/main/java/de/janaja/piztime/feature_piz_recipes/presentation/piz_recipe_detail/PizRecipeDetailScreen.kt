package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.util.DeleteDialog
import de.janaja.piztime.feature_piz_recipes.domain.util.EditDialog
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.*
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest

private fun navigateBack(navController: NavController){
    // navigate to home screen to trigger launched effect to reload data, but without it being in the route two times
    navController.navigate(Screen.PizRecipesScreen.route) {
        popUpTo(Screen.PizRecipesScreen.route) { inclusive = true }
    }
}
@Composable
fun PizRecipeDetailScreen(
    navController: NavController,
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    val amountState = viewModel.detailAmountState.value
    val recipeState = viewModel.pizRecipeState.value
    val alertDialogState = viewModel.detailDeleteDialogState.value
    val dialogState = viewModel.detailEditDialogState.value
    val editState = viewModel.detailEditModeState.value

    val context = LocalContext.current
    BackHandler {
        navigateBack(navController)
    }

    if (recipeState.pizRecipe != null) {
        PizRecipeDetailView(
            modifier = Modifier,//.offset(x = offset.dp),
            recipeState.pizRecipe,
            recipeState.imageBitmap,
            recipeState.firstLaunch,
            amountState.amount,
            alertDialogState.deleteDialog,
            dialogState.editDialogState,
            editState.editMode,
            viewModel.hasUser.value,
            viewModel::onEvent
        )
    } else {
        // TODO beim laden schon die 3 sections mit Überschriften anzeigen
        Text("loading")
    }

    // with key1 = true this only gets executed once and not on recomposition!
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PizRecipeDetailViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.massage, Toast.LENGTH_LONG).show()
                }
                PizRecipeDetailViewModel.UiEvent.NavigateBack -> { navigateBack(navController) }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizRecipeDetailView(
    modifier: Modifier,
    pizRecipeWithDetails: PizRecipeWithDetails,
    imageBitmap: ImageBitmap?,
    firstLaunch: Boolean,
    amount: Int,
    deleteDialog: DeleteDialog,
    editDialogState: EditDialog,
    editMode: Boolean,
    hasUser: Boolean,
    onEvent: (PizRecipeDetailEvent) -> Unit
) {

    val overlap: Dp = dimensionResource(id = R.dimen.topSheetBorderHeight)
    val headerHeight = 150.dp
    val cardModifier = Modifier.background(MaterialTheme.colorScheme.surface)

    Box {
        // edit dialog
        if (deleteDialog != DeleteDialog.None) {
            AlertDialog(
                onDismissRequest = { onEvent(PizRecipeDetailEvent.DismissDeleteDialog) },
                confirmButton = {
                    FilledTonalButton(
                        onClick = {
                                  when(deleteDialog){
                                      DeleteDialog.None -> {}
                                      DeleteDialog.Recipe -> { onEvent(PizRecipeDetailEvent.ClickDeleteRecipe)}
                                      DeleteDialog.Ingredient -> { onEvent(PizRecipeDetailEvent.ClickDeleteIngredient)}
                                      DeleteDialog.Step -> { onEvent(PizRecipeDetailEvent.ClickDeleteStep)}
                                  }

                                  /*onEvent(PizRecipeDetailEvent.DeleteRecipe)*/ },
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .fillMaxWidth()
//                            .wrapContentWidth(align = Alignment.End),
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            "confirm delete recipe",
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { onEvent(PizRecipeDetailEvent.DismissDeleteDialog) },
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .fillMaxWidth()
//                            .wrapContentWidth(align = Alignment.End),
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            "dismiss delete recipe",
                        )
                    }
                },
                title = {Text("Wirklich Löschen?")}
            )
        }
        if (editDialogState != EditDialog.None) {
            Dialog(onDismissRequest = {
                onEvent(PizRecipeDetailEvent.DismissEditDialog)
            }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.8f),
                    shape = RoundedCornerShape(size = 10.dp)
                ) {
                    when (editDialogState) {
                        EditDialog.Header -> {
                            EditHeaderView()
                        }
                        EditDialog.Image -> {
                            EditImageView()
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
                    recipeImage = imageBitmap,
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
        if (hasUser) {
            IconButton(
                onClick = { onEvent(PizRecipeDetailEvent.ToggleEditMode) },
                Modifier
                    .padding(end = 8.dp, top = 8.dp)
                    .size(30.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Edit,
                    "edit recipe",
                    Modifier.fillMaxHeight()
                )

            }
        }
        if (editMode) {
            IconButton(
                onClick = {
                    onEvent(PizRecipeDetailEvent.ShowDeleteDialog(DeleteDialog.Recipe))
                },
                Modifier
                    .padding(end = (8 + 36).dp, top = 8.dp)
                    .size(30.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Delete,
                    "delete recipe",
                    Modifier.fillMaxHeight()
                )

            }
        }
    }
}


@Preview
@Composable
fun PizRecipeDetailViewPreview() {
    PizRecipeDetailView(
        modifier = Modifier.background(Color(0xFFC49E2F)),
        pizRecipeWithDetails = DummyData.DummyPizRecipeWithDetails,
        imageBitmap = ImageBitmap.imageResource(
            LocalContext.current.resources,
            R.drawable.test
        ),
        true,
        amount = 4,
        deleteDialog = DeleteDialog.None,
        editDialogState = EditDialog.None,
        editMode = false,
        true
    ) { }
}


