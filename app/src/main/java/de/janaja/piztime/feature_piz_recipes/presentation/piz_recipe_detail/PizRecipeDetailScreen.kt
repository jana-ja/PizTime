package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.DescriptionView
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
    val ingredientsState = viewModel.pizIngredientsState.value
    val stepsWithIngredientsState = viewModel.pizStepsWithIngredientsState.value

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
        ingredientsState.pizIngredients,
        stepsWithIngredientsState.pizStepsWithIngredients,
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
    pizRecipeEntity: PizRecipe,
    pizIngredientEntities: List<PizIngredient>,
    pizStepsWithIngredientsDto: List<PizStepWithIngredients>,
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit,
    pizOffset: Float,
    pizRotation: Float
) {
    Box {
        // screen content
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(modifier = Modifier.height(100.dp)){
                Text(
                    text = pizRecipeEntity.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = pizRecipeEntity.feature,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp)
                )
            }

            IngredientsView(pizIngredientEntities, amount, increaseAmount, decreaseAmount, Modifier.padding(top = 16.dp))

            DescriptionView(pizStepsWithIngredientsDto, amount, Modifier.padding(top = 16.dp))
        }

        // pizza image
        Box(
            modifier = Modifier
                .offset(pizOffset.dp)
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            Image(
                painter = painterResource(id = pizRecipeEntity.imageResourceId),
                contentDescription = "Image of ${pizRecipeEntity.title}",//stringResource(id = ),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
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
        DummyData.DummyPizRecipe,
        DummyData.DummyIngredients,
        DummyData.DummySteps,
        4,
        { },
        { },
        0f,
        0f
    )
}


