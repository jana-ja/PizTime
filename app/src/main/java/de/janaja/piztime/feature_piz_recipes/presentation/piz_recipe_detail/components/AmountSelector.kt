package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountSelector(
    amount: Int,
    onEvent: (PizRecipeDetailEvent) -> Unit
) {
    Log.i("AmountSelector", "I got recomposed!")

    Row {
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            IconButton(
                onClick = {onEvent(PizRecipeDetailEvent.DecreaseAmount)},
                Modifier.height(40.dp).width(32.dp)
            ) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    "decrease amount",
                    Modifier.fillMaxHeight(1.0f).aspectRatio((32f/16f), true) // TODO find way to make icon slimmer
                )
            }
        }
        Text(
            text = amount.toString(),
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            IconButton(
                onClick = {onEvent(PizRecipeDetailEvent.IncreaseAmount)},
                Modifier.height(40.dp).width(32.dp)
            ) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    "increase amount",
                    Modifier.fillMaxHeight(1.0f).aspectRatio((32f/16f), true)
                )
            }
        }
    }


}

@Preview
@Composable
fun AmountSelectorPreview() {
    AmountSelector(1, {})
}