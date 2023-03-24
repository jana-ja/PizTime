package de.janaja.piztime.feature_piz_recipes.domain.use_case

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class SaveRecipeImageUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(imageName: String, imageBitmap: ImageBitmap){
        repository.saveRecipeImage(imageName, bitmap = imageBitmap)
    }
}