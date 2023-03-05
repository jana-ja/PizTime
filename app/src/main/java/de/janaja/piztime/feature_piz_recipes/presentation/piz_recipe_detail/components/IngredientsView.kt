package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.TopSheetShape
import de.janaja.piztime.feature_piz_recipes.presentation.util.bottomElevation
import de.janaja.piztime.feature_piz_recipes.presentation.util.cut


@Composable
fun IngredientsView(
    modifier: Modifier = Modifier,
    ingredients: List<PizIngredient>,
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit,
    contentModifier: Modifier = Modifier
) {
    Log.i("IngredientsView", "I got recomposed!")

    // TODO this is only here to fix current preview issue with resource dimen values
    val borderHeight: Dp = 100.dp //dimensionResource(id = R.dimen.topSheetBorderHeight).value // TODO look up how to turn to dp properly. roundToPx()?


    Surface(
        modifier = modifier.bottomElevation(),
        shape = TopSheetShape(borderHeight.value),
        color = Color.White,
        shadowElevation = 8.dp
    ) {

        // content


        Column(
            modifier = contentModifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)

        )
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Zutaten für",
                    style = MaterialTheme.typography.titleLarge
                )
                AmountSelector(
                    amount,
                    { increaseAmount() },
                    { decreaseAmount() }
                )
                Text(
                    "Pizzen:",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            LazyColumn(
                Modifier
                    .padding(PaddingValues(start = 16.dp, top = 16.dp))
            ) {

                items(ingredients) { ingredient ->
                    Row(Modifier.fillMaxWidth()) {
                        TableCell(
                            text = "${(ingredient.baseAmount * amount).cut()}g",
                            modifier = Modifier.weight(0.15f)
                        )
                        TableCell(text = ingredient.ingredient, modifier = Modifier.weight(0.85f))
                    }
                }
            }
        }
    }

}

@Composable
fun RowScope.TableCell(
    modifier: Modifier,
    text: String
) {
    Text(
        text = text,
        modifier.padding(bottom = 8.dp)
            //.border(1.dp, Color.BLACK)
            ,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview
@Composable
fun IngredientsViewPreview() {
    IngredientsView(
        modifier = Modifier, ingredients = DummyData.DummyIngredients, amount = 4,
        increaseAmount = {},
        decreaseAmount = {})
}

