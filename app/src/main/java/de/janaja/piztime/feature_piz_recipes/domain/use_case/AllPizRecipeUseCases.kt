package de.janaja.piztime.feature_piz_recipes.domain.use_case

data class AllPizRecipeUseCases(
    val initDbIfEmptyUseCase: InitDbIfEmptyUseCase,
    val getAllPizRecipesUseCase: GetAllPizRecipesUseCase,
    val getAllPizRecipesFlowUseCase: GetAllPizRecipesFlowUseCase,
    val getPizRecipeUseCase: GetPizRecipeUseCase,
    val getPizRecipeWithDetailsUseCase: GetPizRecipeWithDetailsUseCase,
    val getPizRecipeWithDetailsFlowUseCase: GetPizRecipeWithDetailsFlowUseCase,
    val updatePizRecipeUseCase: UpdatePizRecipeUseCase,
    val getIngredientsUseCase: GetIngredientsUseCase,
    val getIngredientUseCase: GetIngredientUseCase,
    val updateIngredientsUseCase: UpdateIngredientsUseCase,
    val updateIngredientUseCase: UpdateIngredientUseCase,
    val getStepsWithIngredientsUseCase: GetStepsWithIngredientsUseCase,
    val getStepWithoutIngredientsUseCase: GetStepWithoutIngredientsUseCase,
    val updateStepsWithIngredientsUseCase: UpdateStepsWithIngredientsUseCase,
    val updateStepUseCase: UpdateStepUseCase
)
