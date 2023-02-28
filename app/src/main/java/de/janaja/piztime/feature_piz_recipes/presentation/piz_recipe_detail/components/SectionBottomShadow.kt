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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import de.janaja.piztime.R


@Composable
fun SectionBottomShadow(
    modifier: Modifier,
    height: Dp =  dimensionResource(R.dimen.sectionTopShadowHeight),
    shadowStrokeWidth: Dp = dimensionResource(R.dimen.sectionTopShadowStrokeWidth)
) {

    Box(modifier = modifier.fillMaxWidth()){
        Column(){
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(bottom = shadowStrokeWidth)
                    .shadow(elevation = shadowStrokeWidth, shape = BottomShadowShape())
                    .background(Color.White)
            )
        }

        Column() {

            Box(modifier = Modifier
                .fillMaxWidth()
                //.fillMaxHeight()
                .weight(1f)
                .background(Color.White))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(height))

        }
    }

}

/**
 * needs matchParentsize as height modifier to act as background
 */
//@Composable
//fun SectionBottomShadow(
//    modifier: Modifier,
//    height: Dp =  dimensionResource(R.dimen.sectionTopShadowHeight),
//    shadowStrokeWidth: Dp = dimensionResource(R.dimen.sectionTopShadowStrokeWidth)
//) {
//    Box(modifier = modifier.fillMaxWidth()){
//        Column(){
//            Spacer(modifier = Modifier.weight(1f))
//            Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(height)
//                        .padding(bottom = shadowStrokeWidth)
//                        .shadow(elevation = shadowStrokeWidth, shape = BottomShadowShape())
//                        .background(Color.White)
//                    )
//        }
//
//        Rect(left = 0f, 0f, height.value, height.value)
//
//        Column() {
//
//            Box(modifier = Modifier
//                .fillMaxWidth()
//                //.fillMaxHeight()
//                .weight(1f)
//                .background(Color.White))
//            Spacer(modifier = Modifier
//                .fillMaxWidth()
//                .height(height))
//
//        }
//    }
//
//}

class BottomShadowShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(Path().apply {

        moveTo(0f, 0f)
        arcTo(
            Rect(
                0f,
                -size.height,
                size.height * 2 ,
                size.height
            ), 180f, -90f, true
        )
        lineTo(
            size.width - size.height,
            size.height
        )
        arcTo(
            Rect(
                size.width - size.height * 2,
                -size.height,
                size.width,
                size.height
            ), 90f, -90f, true
        )
        // close the shape
        lineTo(0f, 0f)

    })
}

class BackgroundAndBottomShadowShape(private val borderHeight: Float) : Shape {
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


@Preview
@Composable
fun SectionBottomShadowPreview(){
    Box(Modifier.background(Color.Green)) {
        SectionBottomShadow(modifier = Modifier.height(400.dp), 40.dp, 5.dp)
    }
}