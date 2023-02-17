package de.janaja.piztime.feature_piz_recipes.data.repository

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizIngredientDao
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizRecipeDao
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val pizRecipeDao: PizRecipeDao,
    private val pizIngredientDao: PizIngredientDao
) : Repository {

    override suspend fun addPizRecipe(pizRecipe: PizRecipe) {
        val newId: Long = pizRecipeDao.addPizRecipe(pizRecipe)

    }

    override suspend fun findPizRecipeWithIngredientsById(id: Long): Pair<PizRecipe, List<PizIngredient>>? {

        pizRecipeDao.findPizRecipeWithIngredientsById(id)?.also {
            if (it.isEmpty()) {
                // maybe recipe has no ingredients. try loading it alone
                pizRecipeDao.findPizRecipeById(id).also { recipe ->
                    recipe?.let { return Pair(recipe, listOf()) }
                }
            } else {
                val pizRecipe = it.keys.first()
                val ingredients: List<PizIngredient> = it.flatMap { entry -> entry.value }
//            it.forEach { entry ->
//                ingredients.addAll(entry.value)
//            }
                return Pair(pizRecipe, ingredients)
            }
        }
        return null
    }

    override fun getAllPizRecipes(): Flow<List<PizRecipe>> {
        return pizRecipeDao.getAllPizRecipes()

    }

    override suspend fun updatePizRecipe(pizRecipe: PizRecipe) {
        pizRecipeDao.updatePizRecipe(pizRecipe)
    }

    private val dummyData = mapOf<PizRecipe, List<PizIngredient>>(
        Pair(
            PizRecipe(
                "Pizza Neapel",
                "Fluffiger Rand",
                "35 35 Poolish\n" +
                        "Let it pool for 4 hours room temperature\n" +
                        "Then let it pool for 12-17 hours cool.\n" +
                        "Put at room tempi 3 hours before processing",
                R.drawable.bsp_piz
            ),
            listOf(
                PizIngredient("Mehl", 149.0),
                PizIngredient("Water", 97.0),
                PizIngredient("Salz", 3.5),
                PizIngredient("Trockenhefe", 0.5),
                PizIngredient("Honig", 0.5)
            )
        ),
        Pair(
            PizRecipe("Pizza Rom", "Dünner Boden", "Poolish: 27 27 pro Piz", R.drawable.bsp_piz),
            listOf(
                PizIngredient("Mehl", 120.0),
                PizIngredient("Wasser", 75.0),
                PizIngredient("Salz", 2.5),
                PizIngredient("Zucker", 1.5),
                PizIngredient("Öl", 1.5),
                PizIngredient("Trockenhefe", 0.5)
            )
        ),
        Pair(PizRecipe("Pizza New York", "Fett sein", "back die piz", R.drawable.bsp_piz), listOf())
    )

    override suspend fun initDbIfEmpty() {
        if (pizRecipeDao.isEmpty()) {
            dummyData.forEach {
                val newId = pizRecipeDao.addPizRecipe(it.key)
                it.value.forEach { ingredient ->
                    ingredient.recipeId = newId
                    pizIngredientDao.addPizIngredient(ingredient)
                }
            }
        }
    }
}