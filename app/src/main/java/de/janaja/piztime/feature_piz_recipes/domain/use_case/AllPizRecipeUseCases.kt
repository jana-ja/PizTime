package de.janaja.piztime.feature_piz_recipes.domain.use_case

data class AllPizRecipeUseCases(
    val initDbIfEmptyUseCase: InitDbIfEmptyUseCase,
    val deleteIngredientUseCase: DeleteIngredientUseCase,
    val deleteStepUseCase: DeleteStepUseCase,
    val getAllPizRecipesUseCase: GetAllPizRecipesUseCase,
    val getAllPizRecipesFlowUseCase: GetAllPizRecipesFlowUseCase,
    val getPizRecipeWithDetailsUseCase: GetPizRecipeWithDetailsUseCase,
    val getPizRecipeWithDetailsFlowUseCase: GetPizRecipeWithDetailsFlowUseCase,
    val getIngredientUseCase: GetIngredientUseCase,
    val updateIngredientUseCase: UpdateIngredientUseCase,
    val getStepWithoutIngredientsUseCase: GetStepWithoutIngredientsUseCase,
    val updateStepUseCase: UpdateStepUseCase
)
