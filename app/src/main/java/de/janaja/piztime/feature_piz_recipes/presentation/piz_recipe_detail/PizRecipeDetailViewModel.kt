package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
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

    // standard detail screen states
    private val _pizRecipeState = mutableStateOf(DetailPizRecipeWithDetailsState())
    val pizRecipeState: State<DetailPizRecipeWithDetailsState> = _pizRecipeState

    private val _detailAmountState = mutableStateOf(DetailAmountState())
    val detailAmountState: State<DetailAmountState> = _detailAmountState

    // edit states
    private val _detailEditDialogState = mutableStateOf(DetailEditDialogState())
    val detailEditDialogState: State<DetailEditDialogState> = _detailEditDialogState

    private val _detailEditModeState = mutableStateOf(DetailEditModeState())
    val detailEditModeState: State<DetailEditModeState> = _detailEditModeState

    private val _editInfoState = mutableStateOf(DetailEditInfoState())
    val editInfoState: State<DetailEditInfoState> = _editInfoState

    private val _editImageState = mutableStateOf(DetailEditImageState())
    val editImageState: State<DetailEditImageState> = _editImageState

    private val _editIngredientState = mutableStateOf(DetailEditIngredientState())
    val editIngredientState: State<DetailEditIngredientState> = _editIngredientState

    private val _editStepState = mutableStateOf(DetailEditStepState())
    val editStepState: State<DetailEditStepState> = _editStepState


    // ui event state
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getPizRecipeWithdetailsJob: Job? = null

    private var currentRecipeId: Long? = null

    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            currentRecipeId = id
            getPizRecipeWithDetails(id)
        }
    }

    sealed class UiEvent {
        data class ShowToast(val massage: String) : UiEvent()
    }

    // standard detail screen
    private fun getPizRecipeWithDetails(id: Long) {
        // observe result
        getPizRecipeWithdetailsJob?.cancel()
        getPizRecipeWithdetailsJob = allPizRecipesUseCases.getPizRecipeWithDetailsFlowUseCase().onEach {
            _pizRecipeState.value = _pizRecipeState.value.copy(
                pizRecipe = it,
                imageBitmap = allPizRecipesUseCases.getRecipeImageUseCase(it.imageName)
            )
        }.launchIn(viewModelScope)

        // load data
        viewModelScope.launch(Dispatchers.IO) {
            allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(id)
        }
    }

    private fun increaseAmount() {
        _detailAmountState.value = _detailAmountState.value.copy(amount = _detailAmountState.value.amount + 1)
    }

    private fun decreaseAmount() {
        if (_detailAmountState.value.amount < 1) return
        _detailAmountState.value = _detailAmountState.value.copy(amount = _detailAmountState.value.amount - 1)
    }

    // events
    // start edit mode
    private fun toggleEditMode() {
        val oldState = _detailEditModeState.value.editMode
        _detailEditModeState.value = _detailEditModeState.value.copy(editMode = !oldState)
    }

    // open edit dialog
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
                        image = recipe.imageName,
                        prepTime = recipe.prepTime.toString()
                    )
                }
            }
        }
    }

    private fun clickEditImage() {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Image)
        // get image from repo
        currentRecipeId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val recipe = allPizRecipesUseCases.getPizRecipeUseCase(it)
                if (recipe != null) {
                    _editImageState.value = _editImageState.value.copy(
                        bitmap = allPizRecipesUseCases.getRecipeImageUseCase(recipe.imageName),
                        imageName = recipe.imageName
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

    // save edit
    private fun saveInfo() {
        val state = _editInfoState.value
        currentRecipeId?.let {
            try {
                val prepTime = state.prepTime.toDouble()
                val pizRecipe = PizRecipe(
                    state.title, state.feature, state.image, prepTime, currentRecipeId!!
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

    private fun saveImage() {
        val state = _editImageState.value
        Log.e("hoho",state.imageName)
        viewModelScope.launch(Dispatchers.IO) {
            if (state.bitmap != null) {
                // save image bitmap
                allPizRecipesUseCases.saveRecipeImageUseCase(state.imageName, state.bitmap)

                //save image ref to recipe
                val recipe = _pizRecipeState.value.pizRecipe
                allPizRecipesUseCases.updateRecipeUseCase(
                    PizRecipe(recipe.title, recipe.feature, state.imageName, recipe.prepTime, recipe.id)
                )
                currentRecipeId?.let {
                    // reload data
                    allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
//                    // (only pass data, don't reload from repo TODO in other places i reload..)
//                    _pizRecipeState.value = _pizRecipeState.value.copy(
//                        imageBitmap = state.bitmap
//                    )
                }
            } else {
                _eventFlow.emit(UiEvent.ShowToast("Wähle ein Bild aus oder bearbeite es um zu speichern."))
            }
        }
        // dismiss dialog
        onEvent(PizRecipeDetailEvent.DismissDialog)

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
                }
                // dismiss dialog
                onEvent(PizRecipeDetailEvent.DismissDialog)

            } catch (e: java.lang.NumberFormatException) {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowToast("Gib eine Kommazahl ein."))
                }
            }
        }
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut."))
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

    // edit state changed
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
    private fun editImage(bitmap: ImageBitmap, urlOrWhatever: String) {
        _editImageState.value = _editImageState.value.copy(
            bitmap = bitmap,
            imageName = urlOrWhatever
        )
    }
    private fun editStepDescription(value: String) {
        _editStepState.value = _editStepState.value.copy(
            description = value
        )
    }

    // edit add/delete
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

    private fun editDeleteIngredient() {
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
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut."))
        }
    }

    private fun editDeleteStep() {
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
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut."))
        }
    }

    // handle events
    fun onEvent(event: PizRecipeDetailEvent) {
        when (event) {
            PizRecipeDetailEvent.LaunchAnimation -> {
                _pizRecipeState.value = _pizRecipeState.value.copy(
                    firstLaunch = false
                )
            }

            PizRecipeDetailEvent.IncreaseAmount -> increaseAmount()
            PizRecipeDetailEvent.DecreaseAmount -> decreaseAmount()

            PizRecipeDetailEvent.ToggleEditMode -> toggleEditMode()

            PizRecipeDetailEvent.ClickEditInfo -> clickEditInfo()
            PizRecipeDetailEvent.ClickEditImage -> { clickEditImage() }
            is PizRecipeDetailEvent.ClickEditIngredient -> clickEditIngredient(
                event.id,
                event.isStepIngredient,
                event.stepId
            )
            is PizRecipeDetailEvent.ClickEditStep -> clickEditStep(event.id)

            PizRecipeDetailEvent.ClickSaveInfo -> saveInfo()
            PizRecipeDetailEvent.ClickSaveImage -> { saveImage() }
            PizRecipeDetailEvent.ClickSaveIngredient -> saveIngredient()
            PizRecipeDetailEvent.ClickSaveStep -> saveStep()

            is PizRecipeDetailEvent.RecipeTitleChanged -> editInfoTitle(event.value)
            is PizRecipeDetailEvent.RecipeFeatureChanged -> editInfoFeature(event.value)
            is PizRecipeDetailEvent.RecipePrepTimeChanged -> {
                editInfoPrepTime(event.value)
            }
            is PizRecipeDetailEvent.IngredientNameChanged -> editIngredientName(event.value)
            is PizRecipeDetailEvent.IngredientAmountChanged -> editIngredientAmount(event.value)
            is PizRecipeDetailEvent.ImageChanged -> editImage(event.bitmap, event.urlOrWhatever)
            is PizRecipeDetailEvent.StepDescriptionChanged -> editStepDescription(event.value)

            is PizRecipeDetailEvent.ClickAddIngredient -> editAddIngredient(
                event.isStepIngredient,
                event.stepId
            )
            PizRecipeDetailEvent.ClickAddStep -> editAddStep()

            PizRecipeDetailEvent.ClickDeleteIngredient -> editDeleteIngredient()
            PizRecipeDetailEvent.ClickDeleteStep -> editDeleteStep()

            PizRecipeDetailEvent.DismissDialog -> {
                _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.None)
            }
        }
    }
}


