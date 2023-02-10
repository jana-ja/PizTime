package de.janaja.piztime.feature_piz_recipes.presentation.util

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe

object PreviewDummies {
    val DummyPizRecipe = PizRecipe("Pizza Neapel","Fluffiger Rand", "This recipe is for a 24 hour prove. I know it sounds like a long time but don’t worry!\n" +
            "Simply make the dough the night before you want to make pizza and you will be good to go on the following evening.\n" +
            "Don’t worry about exact timings, anywhere around 20-28 hours will be fine.\n" +
            "Check out my pizza dough calculator here to find the more precise amount of yeast required. This will usually be between 0.2g – 0.5g depending on the type of yeast and your room temperature.", R.drawable.bsp_piz)
    val DummyIngredients = listOf(PizIngredient("Mehl", 155.0), PizIngredient("Water", 95.0), PizIngredient("Salz", 3.5), PizIngredient("Trockenhefe", 0.0875))
}
