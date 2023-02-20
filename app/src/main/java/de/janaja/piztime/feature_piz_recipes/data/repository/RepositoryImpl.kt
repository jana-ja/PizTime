package de.janaja.piztime.feature_piz_recipes.data.repository

import de.janaja.piztime.feature_piz_recipes.data.data_source.PizIngredientDao
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizRecipeDao
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizStepDao
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizStepIngredientDao
import de.janaja.piztime.feature_piz_recipes.domain.model.*
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val pizRecipeDao: PizRecipeDao,
    private val pizIngredientDao: PizIngredientDao,
    private val pizStepDao: PizStepDao,
    private val pizStepIngredientDao: PizStepIngredientDao
) : Repository {

//    override suspend fun findPizRecipeWithIngredientsById(id: Long): Pair<PizRecipe, List<PizIngredient>>? {
//
//        pizRecipeDao.findPizRecipeWithIngredientsById(id)?.also {
//            if (it.isEmpty()) {
//                // maybe recipe has no ingredients. try loading it alone
//                pizRecipeDao.findPizRecipeById(id).also { recipe ->
//                    recipe?.let { return Pair(recipe, listOf()) }
//                }
//            } else {
//                val pizRecipe = it.keys.first()
//                val ingredients: List<PizIngredient> = it.flatMap { entry -> entry.value }
////            it.forEach { entry ->
////                ingredients.addAll(entry.value)
////            }
//                return Pair(pizRecipe, ingredients)
//            }
//        }
//        return null
//    }

    override fun findPizRecipebyId(id: Long): Flow<PizRecipe?> {
        return pizRecipeDao.findPizRecipeById(id)
    }

    override fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): Flow<List<PizIngredient>> {
        return pizIngredientDao.findPizIngredientsByPizRecipeId(pizRecipeId)
    }

    override fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Flow<Map<PizStep, List<PizStepIngredient>>> {
        return pizStepDao.findPizStepsWithPizStepIngredientsByPizRecipeId(pizRecipeId)
    }


    override fun getAllPizRecipes(): Flow<List<PizRecipe>> {
        return pizRecipeDao.getAllPizRecipes()

    }

    override suspend fun updatePizRecipe(pizRecipe: PizRecipe) {
        pizRecipeDao.updatePizRecipe(pizRecipe)
    }

    override suspend fun updatePizIngredients(pizIngredients: List<PizIngredient>) {
        pizIngredientDao.updatePizIngredients(pizIngredients)
    }

    override suspend fun updatePizStep(pizStep: PizStep) {
        pizStepDao.updatePizStep(pizStep)
    }

    override suspend fun updatePizStepIngredients(pizStepIngredients: List<PizStepIngredient>) {
        pizStepIngredientDao.updatePizStepIngredients(pizStepIngredients)
    }


    override suspend fun initDbIfEmpty() {
        if (pizRecipeDao.isEmpty()) {

            pizRecipeDao.addAllPizRecipes(DummyData.DummyRecipeData)

            DummyData.DummyIngredientData.forEach {
                val recipeId = it.key
                it.value.forEach { ingredient ->
                    ingredient.recipeId = recipeId
                    pizIngredientDao.addPizIngredient(ingredient)
                }
            }

            DummyData.DummyStepData.forEach {
                val recipeId = it.key
                it.value.forEach { stepWithIngredients ->
                    val step = stepWithIngredients.first
                    step.recipeId = recipeId
                    val newId = pizStepDao.addPizStep(step)
                    stepWithIngredients.second.forEach { ingredient ->
                        ingredient.stepId = newId
                        pizStepIngredientDao.addPizStepIngredient(ingredient)
                    }
                }
            }
        }
    }
}