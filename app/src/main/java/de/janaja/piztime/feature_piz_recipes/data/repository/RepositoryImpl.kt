package de.janaja.piztime.feature_piz_recipes.data.repository

import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.data.data_source.PizRecipeDao
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl (private val pizRecipeDao: PizRecipeDao) : Repository {

    override suspend fun addPizRecipe(pizRecipe: PizRecipe) {
        pizRecipeDao.addPizRecipe(pizRecipe)
    }

    override suspend fun findPizRecipeById(id: Long): PizRecipe? {
        return pizRecipeDao.findPizRecipeById(id)
    }

    override fun getAllPizRecipes(): Flow<List<PizRecipe>> {
        return pizRecipeDao.getAllPizRecipes()
    }

    override suspend fun updatePizRecipe(pizRecipe: PizRecipe) {
        pizRecipeDao.updatePizRecipeDetails(pizRecipe)
    }

    private val dummyData = listOf<PizRecipe>(
        PizRecipe("Pizza Neapel","Fluffiger Rand", "njin", R.drawable.bsp_piz),
        PizRecipe("Pizza Rom","Dünner Boden", "", R.drawable.bsp_piz),
        PizRecipe("Pizza New York","Fett sein","", R.drawable.bsp_piz)
    )

    override suspend fun initDbIfEmpty() {
        if(pizRecipeDao.isEmpty()) {
            dummyData.forEach {
                pizRecipeDao.addPizRecipe(it)
            }
        }
    }
}