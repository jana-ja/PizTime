package de.janaja.piztime.feature_piz_recipes.data.repository

import de.janaja.piztime.feature_piz_recipes.data.local.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.mapper.*
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import kotlinx.coroutines.flow.*

class RepositoryImpl( // TODO here db bekommen mit dagger hilt
    private val pizRecipeDao: PizRecipeDao,
    private val pizIngredientDao: PizIngredientDao,
    private val pizStepDao: PizStepDao,
    private val pizStepIngredientDao: PizStepIngredientDao
) : Repository {

    private val _pizRecipeWithDetailsFlow = MutableStateFlow(DummyData.DummyPizRecipeWithDetails)
    override val pizRecipeWithDetailsFlow = _pizRecipeWithDetailsFlow.asStateFlow()

    private val _allPizRecipesFlow = MutableStateFlow(listOf(DummyData.DummyPizRecipe))
    override val allPizRecipesFlow = _allPizRecipesFlow.asStateFlow()

    override suspend fun findPizRecipeWithDetailsById(id: Long) {
        // TODO hier flow bauen und returnen oder member flow haben und dann im viewmodel einmal den flow holen und "observen" und findPizRecipe.. aufrufen.
        // TODO vllt use case layer wegwerfen?
        pizRecipeDao.findPizRecipeById(id)?.let {
            val pizRecipeEntity: PizRecipeEntity = it
            val pizIngredientEntities: List<PizIngredientEntity> =
                pizIngredientDao.findPizIngredientsByPizRecipeId(id)
            val pizStepWithIngredientEntities: List<Pair<PizStepEntity, List<PizStepIngredientEntity>>> =
                mapToList(pizStepDao.findPizStepsWithPizStepIngredientsByPizRecipeId(id))

            _pizRecipeWithDetailsFlow.update {
                EntityCollection(
                    pizRecipeEntity,
                    pizIngredientEntities,
                    pizStepWithIngredientEntities
                ).toPizRecipeWithDetails()
            }
        }
        // TODO update with null? do nothing??
        //return null
    }

    private fun mapToList(map: Map<PizStepEntity, List<PizStepIngredientEntity>>): List<Pair<PizStepEntity, List<PizStepIngredientEntity>>> {
        // convert map to list here
        val stepsWithIngredients =
            mutableListOf<Pair<PizStepEntity, List<PizStepIngredientEntity>>>()
        for (key in map.keys) {
            val value = map[key]
            if (value != null) {
                stepsWithIngredients.add(Pair(key, value))
            } else {
                stepsWithIngredients.add(Pair(key, listOf()))
            }
        }
        return stepsWithIngredients

    }

    override fun findPizRecipebyId(id: Long): Flow<PizRecipeEntity?> {
        return pizRecipeDao.findPizRecipeByIdFlow(id)
    }

    override fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): Flow<List<PizIngredientEntity>> {
        return pizIngredientDao.findPizIngredientsByPizRecipeIdFlow(pizRecipeId)
    }

    override fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Flow<Map<PizStepEntity, List<PizStepIngredientEntity>>> {
        return pizStepDao.findPizStepsWithPizStepIngredientsByPizRecipeIdFlow(pizRecipeId)
    }

    // TODO repository baut den flow, bekommt ihn nicht von room. wenn was inserted wird dann m??sste sich room darum k??mmern neuen flow zu emitten.
    //  aber wie soll das gehen in unterschiedlichen funktionen??

    override suspend fun getAllPizRecipes() {

        _allPizRecipesFlow.update {
            pizRecipeDao.getAllPizRecipes().map { recipeEntity -> recipeEntity.toRecipe() }
        }
    }
    // TODO update with null? do nothing??


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

        DummyData.DummyRecipeWithDetailsData.forEach {
            val bla = it.toEntityCollection()
            pizRecipeDao.addPizRecipe(bla.pizRecipeEntity)
            bla.pizIngredientEntities.forEach { ingredient ->
                pizIngredientDao.addPizIngredient(ingredient)
            }
            bla.pizStepWithIngredientEntities.forEach { stepWithIngredients ->
                val step = stepWithIngredients.first
                val newId = pizStepDao.addPizStep(step)
                stepWithIngredients.second.forEach { ingredient ->
                    ingredient.stepId = newId
                    pizStepIngredientDao.addPizStepIngredient(ingredient)
                }
            }

        }
        getAllPizRecipes()
//                pizRecipeDao.addAllPizRecipes(DummyData.DummyRecipeWithDetailsData.map { recipeWithDetails ->
//                    recipeWithDetails.toPizRecipe().toRecipeEntity()
//                })
//
//                DummyData.DummyRecipeWithDetailsData.forEach {
//                    val recipeId = it.id
//                    it.ingredients.forEach { ingredient ->
//                        ingredient.recipeId = recipeId
//                        pizIngredientDao.addPizIngredient(ingredient)
//                    }
//                }
//
//                DummyData.DummyStepData.forEach {
//                    val recipeId = it.key
//                    it.value.forEach { stepWithIngredients ->
//                        val step = stepWithIngredients.first
//                        step.recipeId = recipeId
//                        val newId = pizStepDao.addPizStep(step)
//                        stepWithIngredients.second.forEach { ingredient ->
//                            ingredient.stepId = newId
//                            pizStepIngredientDao.addPizStepIngredient(ingredient)
//                        }
//                    }
//                }
    }
}
}