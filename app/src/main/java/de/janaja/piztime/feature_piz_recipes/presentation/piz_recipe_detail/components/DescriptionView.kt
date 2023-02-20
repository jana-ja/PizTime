package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStep
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun DescriptionView(
    stepsWithIngredients: List<Pair<PizStep, List<PizStepIngredient>>>,
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
                "${it + 1}:\t\t${stepsWithIngredients[it].first.description}",
                Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp)),
                style = MaterialTheme.typography.bodyMedium
            )
            // step ingredients
            // TODO
        }
    }
}

@Preview
@Composable
fun DescriptionViewPreview() {
    DescriptionView(stepsWithIngredients = DummyData.DummySteps)
}