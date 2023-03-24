package de.janaja.piztime.feature_piz_recipes.domain.use_case

data class AllPizRecipeUseCases(
    val initDbIfEmptyUseCase: InitDbIfEmptyUseCase,

    val loadAllPizRecipesUseCase: LoadAllPizRecipesUseCase,
    val loadPizRecipeWithDetailsUseCase: LoadPizRecipeWithDetailsUseCase,

    val getAllPizRecipesFlowUseCase: GetAllPizRecipesFlowUseCase,
    val getPizRecipeWithDetailsFlowUseCase: GetPizRecipeWithDetailsFlowUseCase,
    val getPizRecipeUseCase: GetPizRecipeUseCase,
    val getRecipeImageUseCase: GetRecipeImageUseCase,
    val getIngredientUseCase: GetIngredientUseCase,
    val getStepWithoutIngredientsUseCase: GetStepWithoutIngredientsUseCase,

    val deleteIngredientUseCase: DeleteIngredientUseCase,
    val deleteStepUseCase: DeleteStepUseCase,

    val updateIngredientUseCase: UpdateIngredientUseCase,
    val updateStepUseCase: UpdateStepUseCase,
    val updateRecipeUseCase: UpdateRecipeUseCase,

    val saveRecipeImageUseCase: SaveRecipeImageUseCase
)
