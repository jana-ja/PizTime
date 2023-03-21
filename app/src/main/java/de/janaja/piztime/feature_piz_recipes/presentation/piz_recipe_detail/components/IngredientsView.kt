package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.TopSheetShape
import de.janaja.piztime.feature_piz_recipes.presentation.util.bottomElevation
import de.janaja.piztime.feature_piz_recipes.presentation.util.cut


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IngredientsView(
    modifier: Modifier = Modifier,
    ingredients: List<PizIngredient>,
    amount: Int,
    onEvent: (PizRecipeDetailEvent) -> Unit,
    contentModifier: Modifier = Modifier,
    editMode: Boolean
) {
    Log.i("IngredientsView", "I got recomposed!")

    // TODO this is only here to fix current preview issue with resource dimen values
    val borderHeight: Dp =
        100.dp //dimensionResource(id = R.dimen.topSheetBorderHeight).value // TODO look up how to turn to dp properly. roundToPx()?


    Surface(
        modifier = modifier.bottomElevation(),
        shape = TopSheetShape(borderHeight.value),
        shadowElevation = 8.dp
    ) {

        // content

        Box(
            modifier = contentModifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)

        ) {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                // header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        "Zutaten für", style = MaterialTheme.typography.titleLarge
                    )
                    AmountSelector(
                        amount, onEvent
                    )
                    Text(
                        "Pizzen:", style = MaterialTheme.typography.titleLarge
                    )
                }

                // content ingredients
                Row(
                    Modifier.padding(PaddingValues(top = 16.dp, end = 16.dp))
                ) {
                    val height = 30.dp
                    var clickableModifier = Modifier.height(height)
                    val textStyle = MaterialTheme.typography.bodyLarge
                    // edit button and amount
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ingredients.forEach { ingredient ->
                            // edit mode
                            if (editMode) {
                                clickableModifier =
                                    clickableModifier
                                        .clickable {
                                            onEvent(PizRecipeDetailEvent.ClickEditIngredient(ingredient.id))
                                        }

                            }
                            Row(modifier = clickableModifier, verticalAlignment = Alignment.CenterVertically) {
                                AnimatedVisibility (editMode) {
                                    Icon(
                                        Icons.Default.Edit,
                                        "edit ingredient",
                                        modifier = Modifier
                                            .padding(start = 32.dp)
                                            .size(height)
                                            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                                            .padding(6.dp)

                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .padding(start = (if(editMode) 16.dp else 32.dp)),
                                    text = "${(ingredient.baseAmount * amount).cut()}g",
                                    style = textStyle
                                )
                            }

                        }
                    }
                    // names
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 16.dp),
                    ) {
                        ingredients.forEach { ingredient ->
                            // edit mode
                            if (editMode) clickableModifier =
                                clickableModifier.clickable {
                                    onEvent(
                                        PizRecipeDetailEvent.ClickEditIngredient(
                                            ingredient.id
                                        )
                                    )
                                }
                            Row(modifier = clickableModifier, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = ingredient.ingredient,

                                    style = textStyle
                                )
                            }
                        }
                    }
                }

                // optional: add ingredient button
                AnimatedVisibility (editMode) {
                    FilledTonalButton(
                        modifier = Modifier.padding(top = 8.dp, start = 32.dp),
                        onClick = { onEvent(PizRecipeDetailEvent.ClickAddIngredient(false)) })
                    {
                        Text(
                            "add Ingredient",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Icon(
                            Icons.Default.Add,
                            "add ingredient"
                        )
                    }

                }
            }
        }
    }

}


@Preview
@Composable
fun IngredientsViewPreview() {
    IngredientsView(
        modifier = Modifier,
        ingredients = DummyData.DummyIngredients,
        amount = 4,
        onEvent = {},
        editMode = true
    )
}

