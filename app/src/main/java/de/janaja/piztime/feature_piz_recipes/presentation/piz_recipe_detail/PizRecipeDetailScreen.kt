package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.HeaderView
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.StepsView
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.IngredientsView
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun PizRecipeDetailScreen(
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    val amountState = viewModel.detailAmountState.value
    val recipeState = viewModel.pizRecipeState.value
//    val ingredientsState = viewModel.pizIngredientsState.value
//    val stepsWithIngredientsState = viewModel.pizStepsWithIngredientsState.value

    //  TODO extra states for edit

    PizRecipeDetailView(
        modifier = Modifier,//.offset(x = offset.dp),
        recipeState.pizRecipe,
        amountState.amount,
        { viewModel.increaseAmount() },
        { viewModel.decreaseAmount() },
    )


}

@Composable
fun PizRecipeDetailView(
    modifier: Modifier,
    pizRecipeWithDetails: PizRecipeWithDetails,
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit
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
            // if sections should have different background colors or have no gaps in general,
            // then they have to overlap (negative space in layout arrangement and extra padding for the views tops)

            HeaderView(
                modifier = Modifier.zIndex(1f),
                title = pizRecipeWithDetails.title,
                feature = pizRecipeWithDetails.feature,
                imageResId = pizRecipeWithDetails.imageResourceId,
                contentModifier = Modifier,
                height = headerHeight
            )

            IngredientsView(
                modifier = Modifier.zIndex(.9f),
                ingredients = pizRecipeWithDetails.ingredients,
                amount = amount,
                increaseAmount = increaseAmount,
                decreaseAmount = decreaseAmount,
                contentModifier = Modifier.padding(top = overlap),

                )

            StepsView(
                modifier = Modifier.zIndex(.8f),
                stepsWithIngredients = pizRecipeWithDetails.steps,
                amount = amount,
                contentModifier = Modifier.padding(top = overlap)

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
        { },
        { }
    )
}


