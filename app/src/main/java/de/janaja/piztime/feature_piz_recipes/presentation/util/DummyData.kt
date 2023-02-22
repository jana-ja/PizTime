package de.janaja.piztime.feature_piz_recipes.presentation.util

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity

object DummyData {
    // preview
    val DummyPizRecipeEntity = PizRecipeEntity("Pizza Neapel", "Fluffiger Rand", R.drawable.bsp_piz, id = 1)
    val DummyIngredients = listOf(
        PizIngredientEntity("Mehl", 149.0),
        PizIngredientEntity("Water", 97.0),
        PizIngredientEntity("Salz", 3.5),
        PizIngredientEntity("Trockenhefe", 0.5),
        PizIngredientEntity("Honig", 0.5)
    )
    val DummySteps = listOf(
        Pair(
            PizStepEntity("Erstmal den Poolish machen mit gleichen Teilen Mehl und Wasser."), listOf(
                PizStepIngredientEntity("Mehl", 35.0),
                PizStepIngredientEntity("Wasser", 35.0)
            )
        ),
        Pair(
            PizStepEntity("Hier ist jetzt ein erfundener Schritt mit einem längeren Text und vielen Zutaten." +
                    "Die werden alle verwendet um ne gute Piz zumachen."), listOf(
                PizStepIngredientEntity("Zutat 1", 24.0),
                PizStepIngredientEntity("Zutat 2", 24.0),
                PizStepIngredientEntity("Zutat 3", 24.0),
                PizStepIngredientEntity("Zutat mit einem langen Namen der ist so lang dass der Hoffentlich nicht in eine Reihe passt und abgeschnitten wird.", 24.0),
                PizStepIngredientEntity("Zutat 1", 24.0),
                PizStepIngredientEntity("TestIngr2", 74.0),
                PizStepIngredientEntity("TestIngr3", 4.0)
            )
        )
    )

    // db init
    val DummyRecipeData = listOf(
        DummyPizRecipeEntity,
        PizRecipeEntity("Pizza Rom", "Dünner Boden", R.drawable.bsp_piz, id = 2),
        PizRecipeEntity("Pizza New York", "Fett sein", R.drawable.bsp_piz, id = 3)
    )

    val DummyIngredientData = mapOf<Long, List<PizIngredientEntity>>(
        Pair(
            1,
            DummyIngredients
        ),
        Pair(
            2,
            listOf(
                PizIngredientEntity("Mehl", 120.0),
                PizIngredientEntity("Wasser", 75.0),
                PizIngredientEntity("Salz", 2.5),
                PizIngredientEntity("Zucker", 1.5),
                PizIngredientEntity("Öl", 1.5),
                PizIngredientEntity("Trockenhefe", 0.5)
            )
        )
    )

    val DummyStepData = mapOf<Long, List<Pair<PizStepEntity, List<PizStepIngredientEntity>>>>(
        Pair(
            1,
            listOf(
                Pair(
                    PizStepEntity("Poolish machen"), listOf(
                    PizStepIngredientEntity("Mehl", 35.0),
                    PizStepIngredientEntity("Wasser", 35.0)
                )),
                Pair(PizStepEntity("Let it pool for 4 hours room temperature"), listOf()),
                Pair(PizStepEntity("Then let it pool for 12-17 hours cool."), listOf()),
                Pair(PizStepEntity("Put at room tempi 3 hours before processing"), listOf())
            )
        ),
        Pair(
            2,
            listOf(
                Pair(
                    PizStepEntity("Poolish machen"), listOf(
                    PizStepIngredientEntity("Mehl", 27.0),
                    PizStepIngredientEntity("Wasser", 27.0)
                ))
            )
        ),
        Pair(
            3,
            listOf(
                Pair(PizStepEntity("back die piz"), listOf())
            )
        )
    )

}
