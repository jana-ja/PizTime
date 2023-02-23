package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.SimpleFlowRow
import de.janaja.piztime.feature_piz_recipes.presentation.util.cut

@Composable
fun DescriptionView(
    stepsWithIngredients: List<PizStepWithIngredients>,
    amount: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        Log.i("DescriptionView", "I got recomposed!")

        item {
            Text(
                "Rezept:",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(stepsWithIngredients.size) {
            // step description
            Text(
                "${it + 1}:\t\t${stepsWithIngredients[it].description}",
                Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp)),
                style = MaterialTheme.typography.bodyMedium
            )
            // step ingredients
            SimpleFlowRow(
                verticalGap = 8.dp,
                horizontalGap = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                for (ingredient in stepsWithIngredients[it].ingredients) {
                    Text(
                        text = "${ingredient.ingredient}: ${(ingredient.baseAmount * amount).cut()}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun DescriptionViewPreview() {
    DescriptionView(stepsWithIngredients = DummyData.DummySteps, amount = 4)
}

