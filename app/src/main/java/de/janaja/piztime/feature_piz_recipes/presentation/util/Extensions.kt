package de.janaja.piztime.feature_piz_recipes.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp


fun Double.cut(): String {
    return if(this % 1 == 0.0) this.toInt().toString() else "%.${2}f".format(this)
}

fun Modifier.bottomElevation(): Modifier = this.then(Modifier.drawWithContent {
    val paddingPx = 24.dp.toPx() // TODO find the right value here, different elevation levels (zIndex) have different length of shadows event with same shadowElevation Value
    clipRect(
        left = 0f,
        top = 0f,
        right = size.width,
        bottom = size.height + paddingPx
    ) {
        this@drawWithContent.drawContent()
    }
})