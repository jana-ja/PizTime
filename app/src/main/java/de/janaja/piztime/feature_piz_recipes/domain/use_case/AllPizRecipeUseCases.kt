package de.janaja.piztime.feature_piz_recipes.domain.use_case

data class AllPizRecipeUseCases(
    val initDbIfEmptyUseCase: InitDbIfEmptyUseCase,
    val getAllPizRecipesUseCase: GetAllPizRecipesUseCase,
    val getPizRecipeUseCase: GetPizRecipeUseCase,
    val getPizRecipeWithDetailsUseCase: GetPizRecipeWithDetailsUseCase,
    val updatePizRecipeUseCase: UpdatePizRecipeUseCase,
    val getIngredientsUseCase: GetIngredientsUseCase,
    val updateIngredientsUseCase: UpdateIngredientsUseCase,
    val getStepsWithIngredientsUseCase: GetStepsWithIngredientsUseCase,
    val updateStepsWithIngredientsUseCase: UpdateStepsWithIngredientsUseCase
)
