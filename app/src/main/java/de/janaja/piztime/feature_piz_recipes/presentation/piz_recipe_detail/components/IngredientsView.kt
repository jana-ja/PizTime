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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.cut


@Composable
fun IngredientsView(
    ingredients: List<PizIngredient>,
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit,
    contentModifier: Modifier = Modifier,
    backGroundModifier: Modifier = Modifier
) {
    Log.i("IngredientsView", "I got recomposed!")

    val column1Weight = .2f
    val column2Weight = .8f

    /*
    Box(
        modifier = backGroundModifier
    ) {
        // background
        SectionBottomShadow(
            modifier = Modifier.matchParentSize()
        )

        // content
*/

    // TODO ich könnte surface mit der großen shape benutzen udn die bottomElevation extension function damit nur unten shadow ist.
    //  das gleiche ergebnis bekomme ich auch mit meinem custom box backgroudn zeugs
    //  wenn ich aber will dass das auch gut aussieht ohen dass diese dinge und der gesamte backgroudn die gleiche farbe haben muss ich mit offsets arbeiten.
    Surface(
        modifier = backGroundModifier.bottomElevation(),
        shape = BackgroundAndBottomShadowShape(100.dp.value),
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
                            text = (ingredient.baseAmount * amount).cut(),
                            weight = column1Weight
                        )
                        TableCell(text = ingredient.ingredient, weight = column2Weight)
                    }
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
    IngredientsView(ingredients = DummyData.DummyIngredients, amount = 4, {}, {})
}

private fun Modifier.bottomElevation(): Modifier = this.then(Modifier.drawWithContent {
    val paddingPx = 8.dp.toPx()
    clipRect(
        left = 0f,
        top = 0f,
        right = size.width,
        bottom = size.height + paddingPx
    ) {
        this@drawWithContent.drawContent()
    }
})