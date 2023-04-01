package de.janaja.piztime.feature_piz_recipes.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp


fun Double.cut(): String {
    return "%.${2}f".format(this).replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "").replace(",$".toRegex(),"")
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

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }