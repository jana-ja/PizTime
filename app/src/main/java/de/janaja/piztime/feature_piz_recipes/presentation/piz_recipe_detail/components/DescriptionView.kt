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

@Composable
fun DescriptionView(
    description: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        Log.i("DescriptionView", "I got recomposed!")
        item {
            Text("Rezept:",
                style = MaterialTheme.typography.titleMedium)
        }
        val lines = description.split("\n")
        items(lines.size){
            Text(
                "${it+1}:\t\t${lines[it]}",
                Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp)),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun DescriptionViewPreview(){
    DescriptionView(description = "Anleitung Schritt 1. \nHier ist Schritt 2")
}