package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.launch

@Composable
fun PizRecipeDetailScreen(
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val amountState = viewModel.detailAmountState.value
    val recipeState = viewModel.pizRecipeState.value
//    val ingredientsState = viewModel.pizIngredientsState.value
//    val stepsWithIngredientsState = viewModel.pizStepsWithIngredientsState.value

    //  TODO extra states for edit

    // animation stuff
    val animDuration = 1100
    var screenVisible by remember {
        mutableStateOf(false)
    }
    val transition = updateTransition(targetState = screenVisible, null)
//    val offset by transition.animateFloat(
//        transitionSpec = { tween(animDuration, easing = LinearOutSlowInEasing) },
//        label = ""
//    ) {
//        if (it) 0f else 200f
//    }

    val pizOffset by transition.animateFloat(
        transitionSpec = { tween(animDuration, easing = LinearOutSlowInEasing) },
        label = ""
    ) {
        if (it) 0f else -400f
    }
    val pizRotation by transition.animateFloat(
        transitionSpec = { tween(animDuration, easing = LinearOutSlowInEasing) },
        label = ""
    ) {
        if (it)
            0f
        else
            -360f
    }

    PizRecipeDetailView(
        modifier = Modifier,//.offset(x = offset.dp),
        recipeState.pizRecipe,
        amountState.amount,
        { viewModel.increaseAmount() },
        { viewModel.decreaseAmount() },
        pizOffset,
        pizRotation
    )


    SideEffect {
        coroutineScope.launch {
            screenVisible = true
//                delay((animDuration / 2).toLong())
//                cardVisible = true
        }
    }
}

@Composable
fun PizRecipeDetailView(
    modifier: Modifier,
    pizRecipeWithDetails: PizRecipeWithDetails,
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit,
    pizOffset: Float,
    pizRotation: Float
) {

    val overlap: Dp = dimensionResource(id = R.dimen.topSheetBorderHeight)
    val headerHeight = 150.dp

    Box {
        // screen content
        Column(
            modifier = modifier
                .fillMaxSize()
//                .background(Color.White)

        ) {

            HeaderView(
                modifier = Modifier.zIndex(1f),
                title = pizRecipeWithDetails.title,
                feature = pizRecipeWithDetails.feature,
                contentModifier = Modifier,
                height = headerHeight
            )

            // if sections should have different background colors or have no gaps in general,
            // then they have to overlap with offset and padding (offset for the whole thing and padding for the content)
            IngredientsView(
                modifier = Modifier.offset(y = -overlap),
                ingredients = pizRecipeWithDetails.ingredients,
                amount = amount,
                increaseAmount = increaseAmount,
                decreaseAmount = decreaseAmount,
                contentModifier = Modifier.padding(top = overlap),

            )

            // TODO komische offset überlappungs sache vllt mit constraintlayout lösen.
            StepsView(
                modifier = Modifier,
                stepsWithIngredients = pizRecipeWithDetails.steps,
                amount = amount,
                contentModifier = Modifier

            )

        }

        // TODO kann ich die piz jetzt doch in den header packen, weil der header nicht animiert wird?
        // pizza image
        Box(
            modifier = Modifier
                .offset(pizOffset.dp)
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            Image(
                painter = painterResource(id = pizRecipeWithDetails.imageResourceId),
                contentDescription = "Image of ${pizRecipeWithDetails.title}",//stringResource(id = ),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(headerHeight / 3 * 2)
                    .rotate(pizRotation)

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
        { },
        0f,
        0f
    )
}


