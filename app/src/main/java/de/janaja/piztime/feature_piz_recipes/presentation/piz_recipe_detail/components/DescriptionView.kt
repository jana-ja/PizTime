package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailViewModel

@Composable
fun DescriptionView(
    description: String
) {
    Column(Modifier.padding(16.dp)) {
        Log.i("DescriptionView", "I got recomposed!")
        Text("Rezept:",
        style = MaterialTheme.typography.bodyLarge)
        Text(
            description,
            Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp)),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}