package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AmountSelector(
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit
) {
    Log.i("AmountSelector", "I got recomposed!")
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Anzahl der Pizzen:",
            style = MaterialTheme.typography.bodyLarge
            )

        Row(
            Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp))
        ) {


            IconButton(onClick = decreaseAmount, Modifier.padding(0.dp)) {
                Icon(Icons.Default.KeyboardArrowLeft,
                    "decrease amount",
                    Modifier.size(46.dp))
            }

            Text(
                text = amount.toString(),
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = increaseAmount) {
                Icon(Icons.Default.KeyboardArrowRight,
                    "increase amount",
                Modifier.size(46.dp))
            }
        }
    }

}

@Preview
@Composable
fun AmountSelectorPreview() {
    AmountSelector(1, {}, {})
}