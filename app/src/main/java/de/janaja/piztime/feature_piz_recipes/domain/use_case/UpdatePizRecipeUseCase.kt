package de.janaja.piztime.feature_piz_recipes.domain.use_case

import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class UpdatePizRecipeUseCase (
    private val repository: Repository
    ){
        suspend operator fun invoke(pizRecipeEntity: PizRecipeEntity) {
            repository.updatePizRecipe(pizRecipeEntity)
        }
    }