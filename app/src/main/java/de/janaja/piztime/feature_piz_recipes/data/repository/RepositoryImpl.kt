package de.janaja.piztime.feature_piz_recipes.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import de.janaja.piztime.feature_piz_recipes.data.local.*
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.mapper.EntityCollection
import de.janaja.piztime.feature_piz_recipes.data.mapper.toEntityCollection
import de.janaja.piztime.feature_piz_recipes.data.mapper.toPizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.data.mapper.toRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.*


class RepositoryImpl(
    db: PizRecipeDatabase,
    private val context: Context
) : Repository {

    private val pizRecipeDao: PizRecipeDao = db.pizRecipeDao
    private val pizIngredientDao: PizIngredientDao = db.pizIngredientDao
    private val pizStepDao: PizStepDao = db.pizStepDao
    private val pizStepIngredientDao: PizStepIngredientDao = db.pizStepIngredientDao

    private val _pizRecipeWithDetailsFlow = MutableStateFlow(DummyData.DummyPizRecipeWithDetails)
    override val pizRecipeWithDetailsFlow = _pizRecipeWithDetailsFlow.asStateFlow()

    private val _allPizRecipesFlow = MutableStateFlow(listOf(DummyData.DummyPizRecipe))
    override val allPizRecipesFlow = _allPizRecipesFlow.asStateFlow()

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
            loadAllPizRecipes()
        }
    }

    // load to flows
    override suspend fun loadAllPizRecipes() {

        _allPizRecipesFlow.update {
            pizRecipeDao.getAllPizRecipes().map { recipeEntity -> recipeEntity.toRecipe() }
        }
    }

    override suspend fun loadPizRecipeWithDetails(id: Long) {

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


    // get
    override suspend fun getPizRecipe(id: Long): PizRecipe? {
        return pizRecipeDao.findPizRecipeById(id)?.toRecipe()
    }

    override suspend fun getRecipeImage(urlOrWhatever: String): ImageBitmap? {
        Log.e("Repo","Get Image")
        if(urlOrWhatever == "")
            return null
        try {
            val f = File(context.filesDir, "$urlOrWhatever.png")
            return withContext(Dispatchers.IO) {
                BitmapFactory.decodeStream(FileInputStream(f))
            }.asImageBitmap()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getPizIngredient(id: Long): PizIngredientEntity {
        return pizIngredientDao.getPizIngredient(id)
    }

    override suspend fun getPizStepIngredient(id: Long): PizStepIngredientEntity {
        return pizStepIngredientDao.getPizStepIngredient(id)
    }

    override suspend fun getPizStep(id: Long): PizStepEntity {
        return pizStepDao.getStep(id)
    }


    // find by
    override suspend fun findPizIngredientsByPizRecipeId(pizRecipeId: Long): List<PizIngredientEntity> {
        return pizIngredientDao.findPizIngredientsByPizRecipeIdFlow(pizRecipeId)
    }

    override fun findPizStepsWithIngredientsByPizRecipeId(pizRecipeId: Long): Map<PizStepEntity, List<PizStepIngredientEntity>> {
        return pizStepDao.findPizStepsWithPizStepIngredientsByPizRecipeIdFlow(pizRecipeId)
    }


    // delete
    override suspend fun deletePizStepWithIngredients(id: Long) {
        pizStepDao.deletePizStep(id)
        pizStepIngredientDao.deletePizStepIngredientsForStepId(id)
    }

    override suspend fun deletePizStepIngredient(id: Long) {
        pizStepIngredientDao.deletePizStepIngredient(id)
    }

    override suspend fun deletePizIngredient(id: Long) {
        pizIngredientDao.deletePizIngredient(id)
    }

    override suspend fun deletePizIngredientsForRecipeId(recipeId: Long) {
        pizIngredientDao.deletePizIngredientsForRecipeId(recipeId)
    }

    override suspend fun deletePizStepsForRecipeId(recipeId: Long) {
        pizStepDao.deletePizStepsForRecipeId(recipeId)
    }

    override suspend fun deletePizStepIngredientsForStepId(stepId: Long) {
        pizStepIngredientDao.deletePizStepIngredientsForRecipeId(stepId)
    }


    // insert
    override suspend fun insertPizIngredients(pizIngredientEntities: List<PizIngredientEntity>) {
        pizIngredientDao.insertPizIngredients(pizIngredientEntities)
    }

    override suspend fun insertPizIngredient(pizIngredientEntity: PizIngredientEntity) {
        pizIngredientDao.insertPizIngredient(pizIngredientEntity)
    }

    override suspend fun insertPizStepIngredient(pizStepIngredientEntity: PizStepIngredientEntity) {
        pizStepIngredientDao.insertPizStepIngredient(pizStepIngredientEntity)
    }

    override suspend fun insertPizSteps(pizStepEntities: List<PizStepEntity>) {
        pizStepDao.insertPizSteps(pizStepEntities)
    }

    override suspend fun insertPizStep(pizStepEntity: PizStepEntity) {
        pizStepDao.insertPizStep(pizStepEntity)
    }

    override suspend fun insertPizStepIngredients(pizStepIngredientEntities: List<PizStepIngredientEntity>) {
        pizStepIngredientDao.insertPizStepIngredients(pizStepIngredientEntities)
    }


    // save
    override suspend fun saveRecipeImage(urlOrWhatever: String, bitmap: ImageBitmap){
        Log.e("Repo","Save Image")
        val f = File(context.filesDir, "$urlOrWhatever.png")
        withContext(Dispatchers.IO) {
            if (!f.exists()) {

                f.createNewFile()
            }

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(f)

                val androidBitmap = bitmap.asAndroidBitmap()
                androidBitmap.setHasAlpha(true) // important for saving transparent background
                androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.e("ARRRRR", "EHOFIH")
            } finally {
                try {
                    fos?.let { fos.close() }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    // update
    override suspend fun updatePizRecipe(pizRecipeEntity: PizRecipeEntity) {
        pizRecipeDao.updatePizRecipe(pizRecipeEntity)
    }


    // helper
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

}