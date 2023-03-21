package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipeDetailViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailAmountState = mutableStateOf(DetailAmountState())
    val detailAmountState: State<DetailAmountState> = _detailAmountState

    private val _detailEditDialogState = mutableStateOf(DetailEditDialogState())
    val detailEditDialogState: State<DetailEditDialogState> = _detailEditDialogState

    private val _detailEditModeState = mutableStateOf(DetailEditModeState())
    val detailEditModeState: State<DetailEditModeState> = _detailEditModeState

    private val _pizRecipeState = mutableStateOf(DetailPizRecipeWithDetailsState())
    val pizRecipeState: State<DetailPizRecipeWithDetailsState> = _pizRecipeState

    private val _editIngredientState = mutableStateOf(DetailEditIngredientState())
    val editIngredientState: State<DetailEditIngredientState> = _editIngredientState

    private val _editStepState = mutableStateOf(DetailEditStepState())
    val editStepState: State<DetailEditStepState> = _editStepState

    private val _editInfoState = mutableStateOf(DetailEditInfoState())
    val editInfoState: State<DetailEditInfoState> = _editInfoState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentRecipeId: Long? = null

    private var getPizRecipeWithdetailsJob: Job? = null


    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            currentRecipeId = id
            getPizRecipeWithDetails(id)
        }
    }

    sealed class UiEvent{
        data class ShowToast(val massage: String): UiEvent()
    }

    private fun getPizRecipeWithDetails(id: Long) {
        // observe result
        getPizRecipeWithdetailsJob?.cancel()
        getPizRecipeWithdetailsJob = allPizRecipesUseCases.getPizRecipeWithDetailsFlowUseCase().onEach {
            _pizRecipeState.value = _pizRecipeState.value.copy(
                pizRecipe = it
            )
        }.launchIn(viewModelScope)

        // load data
        viewModelScope.launch(Dispatchers.IO) {
            allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(id)
        }
    }

    private fun toggleEditMode() {
        val oldState = _detailEditModeState.value.editMode
        _detailEditModeState.value = _detailEditModeState.value.copy(editMode = !oldState)
    }

    private fun clickEditInfo() {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Header)
        // load data to edit info state
        currentRecipeId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val recipe = allPizRecipesUseCases.getPizRecipeUseCase(it)
                if (recipe != null) {
                    _editInfoState.value = _editInfoState.value.copy(
                        title = recipe.title,
                        feature = recipe.feature,
                        imageResId = recipe.imageResourceId,
                        prepTime = recipe.prepTime.toString()
                    )
                }
            }
        }
    }

    private fun clickEditIngredient(id: Long, isStepIngredient: Boolean, stepId: Long) {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Ingredient)
        // load correct ingredient to edit ingredient state
        if (isStepIngredient && currentRecipeId == null) {
            return
        }
        val mapId = if (isStepIngredient) stepId else currentRecipeId!!

        viewModelScope.launch(Dispatchers.IO) {
            val ingredient = allPizRecipesUseCases.getIngredientUseCase(id, isStepIngredient)
            withContext(Dispatchers.Main) {
                _editIngredientState.value = _editIngredientState.value.copy(
                    id = id,
                    ingredientName = ingredient.ingredient,
                    ingredientAmount = ingredient.baseAmount.toString(),
                    isStepIngredient = isStepIngredient,
                    mapId = mapId,
                )
            }
        }
    }

    private fun clickEditStep(id: Long) {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Step)
        viewModelScope.launch(Dispatchers.IO) {
            val stepWithoutIngredients = allPizRecipesUseCases.getStepWithoutIngredientsUseCase(id)
            withContext(Dispatchers.Main) {
                _editStepState.value = _editStepState.value.copy(
                    id = id,
                    description = stepWithoutIngredients.description
                )
            }
        }
    }
    private fun saveInfo() {
        val state = _editInfoState.value
        currentRecipeId?.let {
            try {
                val prepTime = state.prepTime.toDouble()
                val pizRecipe = PizRecipe(
                    state.title, state.feature, state.imageResId, prepTime, currentRecipeId!!
                )

                viewModelScope.launch(Dispatchers.IO) {
                    // insert/update step
                    allPizRecipesUseCases.updateRecipeUseCase(pizRecipe)

                    // reload data
                    allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
                }
                // dismiss dialog
                onEvent(PizRecipeDetailEvent.DismissDialog)

            } catch (e: java.lang.NumberFormatException) {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowToast("Gib eine Kommazahl ein."))
                }
            }
        }
    }

    private fun saveIngredient() {

        val state = _editIngredientState.value

        currentRecipeId?.let {
            try {
                val amount = state.ingredientAmount.toDouble()
                val pizIngredient = PizIngredient(
                    state.ingredientName, amount, state.id
                )

                viewModelScope.launch(Dispatchers.IO) {
                    // insert/update ingredient
                    allPizRecipesUseCases.updateIngredientUseCase(pizIngredient, state.isStepIngredient, state.mapId)

                    // reload data
                    allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)

                    // TODO error handling
                }
                // dismiss dialog
                onEvent(PizRecipeDetailEvent.DismissDialog)

            } catch (e: java.lang.NumberFormatException) {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowToast("Gib eine Kommazahl ein."))
                }
            }

        }

    }

    private fun saveStep() {

        val state = _editStepState.value

        currentRecipeId?.let {
            val pizStepWithIngredient = PizStepWithIngredients(
                state.description, listOf(), state.id
            )

            viewModelScope.launch(Dispatchers.IO) {
                // insert/update step
                allPizRecipesUseCases.updateStepUseCase(pizStepWithIngredient, it)

                // reload data
                allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
            }
            // dismiss dialog
            onEvent(PizRecipeDetailEvent.DismissDialog)

        }

    }

    private fun editInfoTitle(value: String) {
        _editInfoState.value = _editInfoState.value.copy(
            title = value
        )
    }

    private fun editInfoFeature(value: String) {
        _editInfoState.value = _editInfoState.value.copy(
            feature = value
        )
    }

    private fun editInfoPrepTime(value: String) {
        _editInfoState.value = _editInfoState.value.copy(
            prepTime = value
        )
    }

    private fun editIngredientName(value: String) {
        _editIngredientState.value = _editIngredientState.value.copy(
            ingredientName = value
        )
    }

    private fun editIngredientAmount(value: String) {
        _editIngredientState.value = _editIngredientState.value.copy(
            ingredientAmount = value
        )
    }

    private fun editStepDescription(value: String) {
        _editStepState.value = _editStepState.value.copy(
            description = value
        )
    }

    private fun editAddIngredient(isStepIngredient: Boolean, stepId: Long) {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Ingredient)
        // load correct ingredient to edit ingredient state
        if (isStepIngredient && currentRecipeId == null) {
            return
        }
        val mapId = if (isStepIngredient) stepId else currentRecipeId!!

        _editIngredientState.value = _editIngredientState.value.copy(
            id = 0,
            ingredientName = "",
            ingredientAmount = "",
            isStepIngredient = isStepIngredient,
            mapId = mapId,
        )
    }

    private fun editAddStep() {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Step)
        // load correct step to edit ingredient state
        _editStepState.value = _editStepState.value.copy(
            id = 0,
            description = ""
        )

    }

    private fun editDeleteIngredient(){
        currentRecipeId?.let {
            // delete
            val state = _editIngredientState.value
            viewModelScope.launch(Dispatchers.IO) {
                allPizRecipesUseCases.deleteIngredientUseCase(
                    state.id,
                    state.isStepIngredient
                )
                // reload data
                allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
            }
            // dismiss dialog
            onEvent(PizRecipeDetailEvent.DismissDialog)
        }
        // TODO error
    }

    private fun editDeleteStep(){
        currentRecipeId?.let {
            // delete
            viewModelScope.launch(Dispatchers.IO) {
                allPizRecipesUseCases.deleteStepUseCase(
                    _editStepState.value.id
                )
                // reload data
                allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
            }
            // dismiss dialog
            onEvent(PizRecipeDetailEvent.DismissDialog)
        }
        // TODO error
    }


    fun onEvent(event: PizRecipeDetailEvent) {
        when (event) {
            PizRecipeDetailEvent.DecreaseAmount -> decreaseAmount()
            PizRecipeDetailEvent.IncreaseAmount -> increaseAmount()

            PizRecipeDetailEvent.ClickEdit -> toggleEditMode()
            PizRecipeDetailEvent.ClickEditHeader -> clickEditInfo()
            is PizRecipeDetailEvent.ClickEditIngredient -> clickEditIngredient(
                event.id,
                event.isStepIngredient,
                event.stepId
            )
            is PizRecipeDetailEvent.ClickEditStep -> clickEditStep(event.id)
            PizRecipeDetailEvent.DismissDialog -> {
                _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.None)
            }
            PizRecipeDetailEvent.ClickSaveIngredient -> saveIngredient()
            is PizRecipeDetailEvent.IngredientAmountChanged -> editIngredientAmount(event.value)
            is PizRecipeDetailEvent.IngredientNameChanged -> editIngredientName(event.value)
            PizRecipeDetailEvent.ClickSaveStep -> saveStep()
            is PizRecipeDetailEvent.StepDescriptionChanged -> editStepDescription(event.value)
            is PizRecipeDetailEvent.ClickAddIngredient -> editAddIngredient(
                event.isStepIngredient,
                event.stepId
            )
            PizRecipeDetailEvent.ClickAddStep -> editAddStep()
            PizRecipeDetailEvent.ClickDeleteIngredient -> editDeleteIngredient()
            PizRecipeDetailEvent.ClickDeleteStep -> editDeleteStep()
            PizRecipeDetailEvent.ClickSaveInfo -> saveInfo()
            is PizRecipeDetailEvent.RecipeFeatureChanged -> editInfoFeature(event.value)
            is PizRecipeDetailEvent.RecipeTitleChanged -> editInfoTitle(event.value)
            is PizRecipeDetailEvent.RecipePrepTimeChanged -> { editInfoPrepTime(event.value) }
            PizRecipeDetailEvent.LaunchAnimation -> {
                _pizRecipeState.value = _pizRecipeState.value.copy(
                    firstLaunch = false
                )
            }
        }
    }


    private fun increaseAmount() {
        _detailAmountState.value = _detailAmountState.value.copy(amount = _detailAmountState.value.amount + 1)
    }

    private fun decreaseAmount() {
        if (_detailAmountState.value.amount < 1) return
        _detailAmountState.value = _detailAmountState.value.copy(amount = _detailAmountState.value.amount - 1)
    }
}


