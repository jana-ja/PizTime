package de.janaja.piztime.feature_piz_recipes.domain.use_case

data class PizRecipeUseCases(
    val getAllPizRecipesUseCase: GetAllPizRecipesUseCase,
    val getPizRecipeUseCase: GetPizRecipeUseCase,
    val updatePizRecipeUseCase: UpdatePizRecipeUseCase,
    val initDbIfEmptyUseCase: InitDbIfEmptyUseCase
)
