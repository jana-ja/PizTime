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
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.util.PreviewDummies

@Composable
fun IngredientsView(
    ingredients: List<PizIngredient>,
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.i("IngredientsView", "I got recomposed!")


    // Each cell of a column must have the same weight.
    val column1Weight = .3f // 30%
    val column2Weight = .7f // 70%
    // The LazyColumn will be our table. Notice the use of the weights below
    Column(
        modifier = modifier
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Zutaten für",
                style = MaterialTheme.typography.titleMedium
            )
            AmountSelector(
                amount,
                { increaseAmount() },
                { decreaseAmount() }
            )
            Text(
                "Pizzen:",
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyColumn(
            Modifier
                .padding(PaddingValues(start = 16.dp, top = 16.dp))
        ) {
//            item {
//                Row(Modifier.background(Color.GRAY)) {
//                    TableCell(text = "Column 1", weight = column1Weight)
//                    TableCell(text = "Column 2", weight = column2Weight)
//                }
//            }
            items(ingredients) { ingredient ->
                Row(Modifier.fillMaxWidth()) {
                    TableCell(
                        text = "%.${2}f".format(ingredient.baseAmount * amount),
                        weight = column1Weight
                    )
                    TableCell(text = ingredient.ingredient, weight = column2Weight)
                }
            }
        }
    }



}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            //.border(1.dp, Color.BLACK)
            .weight(weight)
            .padding(8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
fun IngredientsViewPreview() {
    IngredientsView(ingredients = PreviewDummies.DummyIngredients, amount = 4, {}, {})
}