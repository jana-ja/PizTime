package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeaderView(
    title: String,
    feature: String,
    contentModifier: Modifier = Modifier,
    backGroundModifier: Modifier = Modifier
) {

    Box(
        modifier = backGroundModifier.fillMaxWidth()
    ) {
        // background
        SectionBottomShadow(
            modifier = Modifier.matchParentSize()
        )

        // content

    /*Surface(
        shape = BackgroundAndBottomShadowShape(100.dp.value),
        color = Color.White,
        shadowElevation = 8.dp
    ) {

        // content
*/
        Column(
            modifier = contentModifier
                .padding(16.dp)
                .height(100.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = feature,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp, start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun HeaderViewPreview() {
    HeaderView(title = "Pizza", feature = "ist lecker")
}