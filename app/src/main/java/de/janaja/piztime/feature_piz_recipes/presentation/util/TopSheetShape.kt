package de.janaja.piztime.feature_piz_recipes.presentation.util

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class TopSheetShape(private val borderHeight: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(Path().apply {

        lineTo(0f, size.height-borderHeight)
        arcTo(
            Rect(
                left = 0f,
                top = size.height-borderHeight*2,
                right = borderHeight * 2,
                bottom = size.height
            ), 180f, -90f, false
        )
        lineTo(
            size.width - borderHeight,
            size.height
        )
        arcTo(
            Rect(
                left = size.width - borderHeight * 2,
                top = size.height-borderHeight*2,
                right = size.width,
                bottom = size.height
            ), 90f, -90f, false
        )
        // close the shape
        lineTo(size.width, 0f)
        lineTo(0f,0f)

    })
}
