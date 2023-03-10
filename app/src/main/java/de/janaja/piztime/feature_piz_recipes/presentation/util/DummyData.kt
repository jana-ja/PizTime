package de.janaja.piztime.feature_piz_recipes.presentation.util

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients

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

        PizStepWithIngredients(
            "Poolish machen", listOf(
                PizIngredient("Mehl", 35.0),
                PizIngredient("Wasser", 35.0)
            )
        ),
        PizStepWithIngredients("Let it pool for 4 hours room temperature", listOf()),
        PizStepWithIngredients("Then let it pool for 12-17 hours cool.", listOf()),
        PizStepWithIngredients("Put at room tempi 3 hours before processing", listOf())

    )
    val DummySteps2 = listOf(

        PizStepWithIngredients(
            "Erstmal den Poolish machen mit gleichen Teilen Mehl und Wasser.", listOf(
                PizIngredient("Mehl", 35.0),
                PizIngredient("Wasser", 35.0)
            )
        ),

        PizStepWithIngredients(
            "Hier ist jetzt ein erfundener Schritt mit einem längeren Text und vielen Zutaten." +
                    "Die werden alle verwendet um ne gute Piz zumachen.", listOf(
                PizIngredient("Zutat 1", 24.0),
                PizIngredient("Zutat 2", 24.0),
                PizIngredient("Zutat 3", 24.0),
                PizIngredient(
                    "Zutat mit einem langen Namen der ist so lang dass der Hoffentlich nicht in eine Reihe passt und abgeschnitten wird.",
                    24.0
                ),
                PizIngredient("Zutat 1", 24.0),
                PizIngredient("TestIngr2", 74.0),
                PizIngredient("TestIngr3", 4.0)
            )
        )
    )
    val DummyPizRecipeWithDetails = PizRecipeWithDetails(
        "Pizza Neapel",
        "Fluffiger Rand",
        R.drawable.bsp_piz,
        DummyIngredients,
        DummySteps,
        id = 1
    )


    // db init

    val DummyRecipeWithDetailsData = listOf(
        PizRecipeWithDetails(
            DummyPizRecipe.title, DummyPizRecipe.feature, DummyPizRecipe.imageResourceId,
            DummyIngredients,
            DummySteps,
            DummyPizRecipe.id
        ),
        PizRecipeWithDetails(
            "Pizza Rom", "Dünner Boden", R.drawable.bsp_piz,
            listOf(
                PizIngredient("Mehl", 120.0),
                PizIngredient("Wasser", 75.0),
                PizIngredient("Salz", 2.5),
                PizIngredient("Zucker", 1.5),
                PizIngredient("Öl", 1.5),
                PizIngredient("Trockenhefe", 0.5)
            ),
            listOf(
                PizStepWithIngredients(
                    "Poolish machen", listOf(
                        PizIngredient("Mehl", 27.0),
                        PizIngredient("Wasser", 27.0)
                    )
                )
            ),
            id = 2
        ),
        PizRecipeWithDetails(
            "Pizza New York", "Fett sein", R.drawable.bsp_piz,
            listOf(
                PizIngredient("1050er Mehl", 148.0),
                PizIngredient("Roggenmehl", 17.0),
                PizIngredient("Wasser", 107.0),
                PizIngredient("Salz", 5.0),
                PizIngredient("Zucker", 2.5),
                PizIngredient("Trockenhefe", 0.8)
            ),
            listOf(PizStepWithIngredients("back die piz", listOf())),
            id = 3
        )
    )

//    val DummyRecipeData = listOf(
//        DummyPizRecipe,
//        PizRecipe("Pizza Rom", "Dünner Boden", R.drawable.bsp_piz, id = 2),
//        PizRecipe("Pizza New York", "Fett sein", R.drawable.bsp_piz, id = 3)
//    )

//    val DummyIngredientData = mapOf<Long, List<PizIngredient>>(
//        Pair(
//            1,
//            DummyIngredients
//        ),
//        Pair(
//            2,
//            listOf(
//                PizIngredient("Mehl", 120.0),
//                PizIngredient("Wasser", 75.0),
//                PizIngredient("Salz", 2.5),
//                PizIngredient("Zucker", 1.5),
//                PizIngredient("Öl", 1.5),
//                PizIngredient("Trockenhefe", 0.5)
//            )
//        )
//    )

//    val DummyStepData = mapOf<Long, List<Pair<PizStepEntity, List<PizStepIngredientEntity>>>>(
//        Pair(
//            1,
//            listOf(
//                Pair(
//                    PizStepEntity("Poolish machen"), listOf(
//                        PizStepIngredientEntity("Mehl", 35.0),
//                        PizStepIngredientEntity("Wasser", 35.0)
//                    )
//                ),
//                Pair(PizStepEntity("Let it pool for 4 hours room temperature"), listOf()),
//                Pair(PizStepEntity("Then let it pool for 12-17 hours cool."), listOf()),
//                Pair(PizStepEntity("Put at room tempi 3 hours before processing"), listOf())
//            )
//        ),
//        Pair(
//            2,
//            listOf(
//                Pair(
//                    PizStepEntity("Poolish machen"), listOf(
//                        PizStepIngredientEntity("Mehl", 27.0),
//                        PizStepIngredientEntity("Wasser", 27.0)
//                    )
//                )
//            )
//        ),
//        Pair(
//            3,
//            listOf(
//                Pair(PizStepEntity("back die piz"), listOf())
//            )
//        )
//    )

}
