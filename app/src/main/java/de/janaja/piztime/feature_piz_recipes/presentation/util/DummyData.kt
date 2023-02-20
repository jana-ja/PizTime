package de.janaja.piztime.feature_piz_recipes.presentation.util

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStep
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepIngredient

object DummyData {
    // preview
    val DummyPizRecipe = PizRecipe("Pizza Neapel", "Fluffiger Rand", R.drawable.bsp_piz, id = 1)
    val DummyIngredients = listOf(
        PizIngredient("Mehl", 149.0),
        PizIngredient("Water", 97.0),
        PizIngredient("Salz", 3.5),
        PizIngredient("Trockenhefe", 0.5),
        PizIngredient("Honig", 0.5)
    )
    val DummySteps = listOf(
        Pair(
            PizStep("Erstmal den Poolish machen mit gleichen Teilen Mehl und Wasser."), listOf(
                PizStepIngredient("Mehl", 35.0),
                PizStepIngredient("Wasser", 35.0)
            )
        ),
        Pair(
            PizStep("Hier ist jetzt ein erfundener Schritt mit einem längeren Text und vielen Zutaten." +
                    "Die werden alle verwendet um ne gute Piz zumachen."), listOf(
                PizStepIngredient("Zutat 1", 24.0),
                PizStepIngredient("Zutat 2", 24.0),
                PizStepIngredient("Zutat 3", 24.0),
                PizStepIngredient("Zutat mit einem langen Namen der ist so lang dass der Hoffentlich nicht in eine Reihe passt und abgeschnitten wird.", 24.0),
                PizStepIngredient("Zutat 1", 24.0),
                PizStepIngredient("TestIngr2", 74.0),
                PizStepIngredient("TestIngr3", 4.0)
            )
        )
    )

    // db init
    val DummyRecipeData = listOf(
        DummyPizRecipe,
        PizRecipe("Pizza Rom", "Dünner Boden", R.drawable.bsp_piz, id = 2),
        PizRecipe("Pizza New York", "Fett sein", R.drawable.bsp_piz, id = 3)
    )

    val DummyIngredientData = mapOf<Long, List<PizIngredient>>(
        Pair(
            1,
            DummyIngredients
        ),
        Pair(
            2,
            listOf(
                PizIngredient("Mehl", 120.0),
                PizIngredient("Wasser", 75.0),
                PizIngredient("Salz", 2.5),
                PizIngredient("Zucker", 1.5),
                PizIngredient("Öl", 1.5),
                PizIngredient("Trockenhefe", 0.5)
            )
        )
    )

    val DummyStepData = mapOf<Long, List<Pair<PizStep, List<PizStepIngredient>>>>(
        Pair(
            1,
            listOf(
                Pair(PizStep("Poolish machen"), listOf(
                    PizStepIngredient("Mehl", 35.0),
                    PizStepIngredient("Wasser", 35.0)
                )),
                Pair(PizStep("Let it pool for 4 hours room temperature"), listOf()),
                Pair(PizStep("Then let it pool for 12-17 hours cool."), listOf()),
                Pair(PizStep("Put at room tempi 3 hours before processing"), listOf())
            )
        ),
        Pair(
            2,
            listOf(
                Pair(PizStep("Poolish machen"), listOf(
                    PizStepIngredient("Mehl", 27.0),
                    PizStepIngredient("Wasser", 27.0)
                ))
            )
        ),
        Pair(
            3,
            listOf(
                Pair(PizStep("back die piz"), listOf())
            )
        )
    )

}
