package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.presentation.util.TopSheetShape
import de.janaja.piztime.feature_piz_recipes.presentation.util.bottomElevation

@Composable
fun HeaderView(
    modifier: Modifier = Modifier,
    title: String,
    feature: String,
    contentModifier: Modifier = Modifier,
    height: Dp
) {


    Surface(
        modifier = modifier.fillMaxWidth().bottomElevation(),
        shape = TopSheetShape(dimensionResource(id = R.dimen.topSheetBorderHeight).value),
        color = Color.White,
        shadowElevation = 8.dp
    ) {

        // content

        Column(
            modifier = contentModifier
                .padding(16.dp)
                .height(height)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = feature,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun HeaderViewPreview() {
    HeaderView(title = "Pizza", feature = "ist lecker", height = 200.dp)
}