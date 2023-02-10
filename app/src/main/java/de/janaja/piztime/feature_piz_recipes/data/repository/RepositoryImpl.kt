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
        // TODO test piz recipe without ingredients

        pizRecipeDao.findPizRecipeWithIngredientsById(id)?.also {
            if (it.isEmpty()) {
                // maybe recipe has no ingredients. try loading it alone
                pizRecipeDao.findPizRecipeById(id).also { recipe ->
                    recipe?.let { return Pair(recipe, listOf()) }
                }
            } else
            {
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
                "This recipe is for a 24 hour prove. I know it sounds like a long time but don’t worry!\n" +
                        "Simply make the dough the night before you want to make pizza and you will be good to go on the following evening.\n" +
                        "Don’t worry about exact timings, anywhere around 20-28 hours will be fine.\n" +
                        "Check out my pizza dough calculator here to find the more precise amount of yeast required. This will usually be between 0.2g – 0.5g depending on the type of yeast and your room temperature.",
                R.drawable.bsp_piz
            ),
            listOf(
                PizIngredient("Mehl", 155.0),
                PizIngredient("Water", 95.0),
                PizIngredient("Salz", 3.5),
                PizIngredient("Trockenhefe", 0.0875)
            )
        ),
        Pair(PizRecipe("Pizza Rom", "Dünner Boden", "njin", R.drawable.bsp_piz), listOf()),
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