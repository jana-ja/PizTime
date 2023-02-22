package de.janaja.piztime.feature_piz_recipes.data.repository

import de.janaja.piztime.feature_piz_recipes.data.local.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import kotlinx.coroutines.flow.Flow

class RepositoryImpl( // TODO here db bekommen mit dagger hilt
    private val pizRecipeDao: PizRecipeDao,
    private val pizIngredientDao: PizIngredientDao,
    private val pizStepDao: PizStepDao,
    private val pizStepIngredientDao: PizStepIngredientDao
) : Repository {

    override fun findPizRecipebyId(id: Long): Flow<PizRecipeEntity?> {
        return pizRecipeDao.findPizRecipeById(id)
    }

    override fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): Flow<List<PizIngredientEntity>> {
        return pizIngredientDao.findPizIngredientsByPizRecipeId(pizRecipeId)
    }

    override fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Flow<Map<PizStepEntity, List<PizStepIngredientEntity>>> {
        return pizStepDao.findPizStepsWithPizStepIngredientsByPizRecipeId(pizRecipeId)
    }

    // TODO repository baut den flow, bekommt ihn nicht von room. wenn was inserted wird dann müsste sich room darum kümmern neuen flow zu emitten.
    //  aber wie soll das gehen in unterschiedlichen funktionen??

    override fun getAllPizRecipes(): Flow<List<PizRecipeEntity>> {
        return pizRecipeDao.getAllPizRecipes()

    }

    override suspend fun updatePizRecipe(pizRecipeEntity: PizRecipeEntity) {
        pizRecipeDao.updatePizRecipe(pizRecipeEntity)
    }

    override suspend fun updatePizIngredients(pizIngredientEntities: List<PizIngredientEntity>) {
        pizIngredientDao.updatePizIngredients(pizIngredientEntities)
    }

    override suspend fun updatePizStep(pizStepEntity: PizStepEntity) {
        pizStepDao.updatePizStep(pizStepEntity)
    }

    override suspend fun updatePizStepIngredients(pizStepIngredientEntities: List<PizStepIngredientEntity>) {
        pizStepIngredientDao.updatePizStepIngredients(pizStepIngredientEntities)
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