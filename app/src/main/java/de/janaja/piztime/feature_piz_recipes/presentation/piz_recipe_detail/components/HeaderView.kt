package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.TopSheetShape
import de.janaja.piztime.feature_piz_recipes.presentation.util.bottomElevation
import de.janaja.piztime.feature_piz_recipes.presentation.util.cut

@Composable
fun HeaderView(
    modifier: Modifier = Modifier,
    recipe: PizRecipe,
    contentModifier: Modifier = Modifier,
    height: Dp,
    onEvent: (PizRecipeDetailEvent) -> Unit,
    editMode: Boolean,
    firstLaunch: Boolean = false
) {

    // animation stuff
    val animDuration = 1100
//    var screenVisible by remember {
//        mutableStateOf(false)
//    }
    val transition = updateTransition(targetState = firstLaunch, null)

    val pizOffset by transition.animateFloat(
        transitionSpec = { tween(animDuration, easing = LinearOutSlowInEasing) },
        label = ""
    ) {
        if (it) -400f else 0f
    }
    val pizRotation by transition.animateFloat(
        transitionSpec = { tween(animDuration, easing = LinearOutSlowInEasing) },
        label = ""
    ) {
        if (it)
            -360f
        else
            0f
    }
    SideEffect {
        onEvent(PizRecipeDetailEvent.LaunchAnimation)
    }



    // content
    val title = recipe.title
    val feature = recipe.feature
    val imageResId = recipe.imageResourceId
    val prepTime = recipe.prepTime

    // TODO this is only here to fix current preview issue with resource dimen values
    val borderHeight: Dp = 100.dp //dimensionResource(id = R.dimen.topSheetBorderHeight).value // TODO look up how to turn to dp properly
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .bottomElevation(),
        shape = TopSheetShape(borderHeight.value),
        shadowElevation = 8.dp
    ) {
        var clickableModifier = Modifier.padding(0.dp)
        if (editMode) {
            clickableModifier =
                clickableModifier
                    .clickable {
                        onEvent(PizRecipeDetailEvent.ClickEditHeader)
                    }
        }
        Box(modifier = contentModifier
            .padding(16.dp)
            .height(height)) {
            Column(
                modifier = clickableModifier
                    .matchParentSize()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (editMode)
                        Icon(
                            Icons.Default.Edit,
                            "edit ingredient",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(30.dp)
                                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                                .padding(6.dp)

                        )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = "Start ${prepTime.cut()} hours early",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // pizza image
            Box(
                modifier = Modifier
                    .offset(pizOffset.dp)
                    .align(Alignment.TopEnd)
                    //.padding(top = 16.dp, end = 8.dp, bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Image of $title",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(height)
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp)
                        .rotate(pizRotation)

                )
            }

        }
    }
}

@Preview
@Composable
fun HeaderViewPreview() {
    HeaderView(
        recipe = DummyData.DummyPizRecipe,
        height = 200.dp,
        onEvent = {},
        editMode = false
    )
}