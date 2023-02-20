package de.janaja.piztime.feature_piz_recipes.presentation.util


fun Double.cut(): String {
    return if(this % 1 == 0.0) this.toInt().toString() else "%.${2}f".format(this)
}