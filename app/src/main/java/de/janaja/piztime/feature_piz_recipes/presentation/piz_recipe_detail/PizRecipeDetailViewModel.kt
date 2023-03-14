package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private var currentRecipeId: Long? = null

    private var getPizRecipeWithdetailsJob: Job? = null


    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            currentRecipeId = id
            getPizRecipeWithDetails(id)
        }
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
            allPizRecipesUseCases.getPizRecipeWithDetailsUseCase(id)
        }
    }

    private fun toggleEditMode() {
        val oldState = _detailEditModeState.value.editMode
        _detailEditModeState.value = _detailEditModeState.value.copy(editMode = !oldState)
    }

    private fun clickEditHeader() {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Header)
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
                    allPizRecipesUseCases.getPizRecipeWithDetailsUseCase(it)

                    // TODO error handling
                }
                // dismiss dialog
                onEvent(PizRecipeDetailEvent.DismissDialog)

            } catch (e: java.lang.NumberFormatException) {
                // TODO error handling
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
                allPizRecipesUseCases.getPizRecipeWithDetailsUseCase(it)
            }
            // dismiss dialog
            onEvent(PizRecipeDetailEvent.DismissDialog)

        }

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


        fun onEvent(event: PizRecipeDetailEvent) {
        when (event) {
            PizRecipeDetailEvent.DecreaseAmount -> decreaseAmount()
            PizRecipeDetailEvent.IncreaseAmount -> increaseAmount()

            PizRecipeDetailEvent.ClickEdit -> toggleEditMode()
            PizRecipeDetailEvent.ClickEditHeader -> clickEditHeader()
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
            is PizRecipeDetailEvent.ClickAddIngredient -> editAddIngredient(event.isStepIngredient,
                event.stepId)
            PizRecipeDetailEvent.ClickAddStep -> editAddStep()
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


