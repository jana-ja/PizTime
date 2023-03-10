package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun StepsView(
    modifier: Modifier = Modifier,
    stepsWithIngredients: List<PizStepWithIngredients>,
    amount: Int,
    contentModifier: Modifier = Modifier,

) {

    Box(
        modifier = modifier.background(Color.White)
    ) {

        LazyColumn(
            modifier = contentModifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Log.i("StepsView", "I got recomposed!")

            item {
                Text(
                    "Rezept:",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(stepsWithIngredients.size) {
                Column {
                    // step description
                    Row{
                        Text(
                            "${it + 1}: ",
                            Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp)),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            stepsWithIngredients[it].description,
                            Modifier.padding(PaddingValues(top = 16.dp)),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // step ingredients
                    if(stepsWithIngredients[it].ingredients.isNotEmpty()) {
                        SimpleFlowRow(
                            verticalGap = 8.dp,
                            horizontalGap = 8.dp,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                        ) {
                            for (ingredient in stepsWithIngredients[it].ingredients) {
                                Text(
                                    text = "${ingredient.ingredient}: ${(ingredient.baseAmount * amount).cut()}g",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        //.background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                                        .padding(4.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}


@Preview
@Composable
fun DescriptionViewPreview() {
    StepsView(stepsWithIngredients = DummyData.DummySteps, amount = 4)
}

