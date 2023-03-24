package de.janaja.piztime.feature_piz_recipes.domain.use_case

import androidx.compose.ui.graphics.ImageBitmap
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository

class GetRecipeImageUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(imageName: String): ImageBitmap? {
        return repository.getRecipeImage(imageName)

    }
}