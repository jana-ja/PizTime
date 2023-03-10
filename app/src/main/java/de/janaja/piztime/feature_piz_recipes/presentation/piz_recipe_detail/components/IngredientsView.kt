package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.TopSheetShape
import de.janaja.piztime.feature_piz_recipes.presentation.util.bottomElevation
import de.janaja.piztime.feature_piz_recipes.presentation.util.cut


@Composable
fun IngredientsView(
    modifier: Modifier = Modifier,
    ingredients: List<PizIngredient>,
    amount: Int,
    onEvent: (PizRecipeDetailEvent) -> Unit,
    contentModifier: Modifier = Modifier
) {
    Log.i("IngredientsView", "I got recomposed!")

    // TODO this is only here to fix current preview issue with resource dimen values
    val borderHeight: Dp =
        100.dp //dimensionResource(id = R.dimen.topSheetBorderHeight).value // TODO look up how to turn to dp properly. roundToPx()?


    Surface(
        modifier = modifier.bottomElevation(),
        shape = TopSheetShape(borderHeight.value),
        color = Color.White,
        shadowElevation = 8.dp
    ) {

        // content

        Box(
            modifier = contentModifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                Column(
                    Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp))
                ) {
                    ingredients.forEach { ingredient ->
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                text = "${(ingredient.baseAmount * amount).cut()}g",
                                Modifier
                                    .weight(0.15f)
                                    .padding(bottom = 8.dp)// TODO find dynamic way for column width
                                //.border(1.dp, Color.BLACK)
                                ,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = ingredient.ingredient,
                                Modifier
                                    .weight(0.85f)
                                    .padding(bottom = 8.dp)
                                //.border(1.dp, Color.BLACK)
                                , style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
            // edit button
            IconButton(
                onClick = { onEvent(PizRecipeDetailEvent.ClickEditIngredients) },
                Modifier
                    .size(36.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    Icons.Default.Edit, "edit recipe", Modifier.fillMaxHeight()
                )

            }
        }
    }

}


@Preview
@Composable
fun IngredientsViewPreview() {
    IngredientsView(modifier = Modifier, ingredients = DummyData.DummyIngredients, amount = 4, onEvent = {})
}

