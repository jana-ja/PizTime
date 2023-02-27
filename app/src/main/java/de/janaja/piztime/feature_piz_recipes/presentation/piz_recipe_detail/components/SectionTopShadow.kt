package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import de.janaja.piztime.R

/**
 * needs matchParentsize as height modifier to act as background
 */
@Composable
fun SectionTopShadow(
    modifier: Modifier,
    height: Dp =  dimensionResource(R.dimen.sectionTopShadowHeight),
    shadowStrokeWidth: Dp = dimensionResource(R.dimen.sectionTopShadowStrokeWidth)
) {
    Column(modifier = modifier) {
        Box(

            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(top = shadowStrokeWidth)
                .shadow(elevation = shadowStrokeWidth, shape = TopShadowShape())
                .background(Color.White)
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White))
    }
}

class TopShadowShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(Path().apply {

        moveTo(0f, size.height)
        arcTo(
            Rect(
                0f,
                0f,
                size.height * 2,
                size.height * 2
            ), 180f, 90f, true
        )
        lineTo(
            size.width - size.height,
            0f
        )
        arcTo(
            Rect(
                size.width - size.height * 2,
                0f,
                size.width,
                size.height * 2
            ), 270f, 90f, true
        )
        // close the shape
        lineTo(0f, size.height)

    })
}