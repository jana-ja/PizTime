package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.util.EditDialog
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.*
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun PizRecipeDetailScreen(
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    val amountState = viewModel.detailAmountState.value
    val recipeState = viewModel.pizRecipeState.value
    val dialogState = viewModel.detailEditDialogState.value
    
    PizRecipeDetailView(
        modifier = Modifier,//.offset(x = offset.dp),
        recipeState.pizRecipe,
        amountState.amount,
        dialogState.editDialogState,
        viewModel::onEvent
    )


}


@Composable
fun PizRecipeDetailView(
    modifier: Modifier,
    pizRecipeWithDetails: PizRecipeWithDetails,
    amount: Int,
    dialogState: EditDialog,
    onEvent: (PizRecipeDetailEvent) -> Unit
) {

    val overlap: Dp = dimensionResource(id = R.dimen.topSheetBorderHeight)
    val headerHeight = 150.dp

    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(-overlap)

        ) {

            // edit dialog

            if (dialogState != EditDialog.None) {
                Dialog(onDismissRequest = {
                    onEvent(PizRecipeDetailEvent.DismissDialog)
                }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.9f),
                        shape = RoundedCornerShape(size = 10.dp)
                    ) {
                        when (dialogState){
                            EditDialog.Header -> {}
                            EditDialog.Ingredients -> EditIngredientsView()
                            EditDialog.Steps -> {}
                            else -> {}
                        }

                    }
                }
            }


            // if sections should have different background colors or have no gaps in general,
            // then they have to overlap (negative space in layout arrangement and extra padding for the views tops)

            HeaderView(
                modifier = Modifier.zIndex(1f),
                title = pizRecipeWithDetails.title,
                feature = pizRecipeWithDetails.feature,
                imageResId = pizRecipeWithDetails.imageResourceId,
                contentModifier = Modifier,
                height = headerHeight,
                onEvent = onEvent
            )

            IngredientsView(
                modifier = Modifier.zIndex(.9f),
                ingredients = pizRecipeWithDetails.ingredients,
                amount = amount,
                onEvent = onEvent,
                contentModifier = Modifier.padding(top = overlap)
                )

            StepsView(
                modifier = Modifier.zIndex(.8f),
                stepsWithIngredients = pizRecipeWithDetails.steps,
                amount = amount,
                contentModifier = Modifier.padding(top = overlap),
                onEvent = onEvent
            )

        }
    }
}


@Preview
@Composable
fun PizRecipeDetailViewPreview() {
    PizRecipeDetailView(
        Modifier.background(Color(0xFFC49E2F)),
        DummyData.DummyPizRecipeWithDetails,
        4,
        EditDialog.None
    ) { }
}


