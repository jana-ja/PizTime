package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.auth.AuthService
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
import java.util.UUID
import javax.inject.Inject


// in clean architecture view model calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipeDetailViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases,
    authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO dont reload data for every edit thing

    // auth
    val hasUser: State<Boolean> = authService.hasUser

    // standard detail screen states
    private val _pizRecipeState = mutableStateOf(DetailPizRecipeWithDetailsState())
    val pizRecipeState: State<DetailPizRecipeWithDetailsState> = _pizRecipeState

    private val _detailAmountState = mutableStateOf(DetailAmountState())
    val detailAmountState: State<DetailAmountState> = _detailAmountState

    // alert dialog state
    private val _detailDeleteDialogState = mutableStateOf(DetailDeleteDialogState())
    val detailDeleteDialogState: State<DetailDeleteDialogState> = _detailDeleteDialogState

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

    private var currentRecipeId: String? = null

    init {
        resetRecipeWithDetails()
        savedStateHandle.get<String>("pizRecipeId")?.let { id ->
            currentRecipeId = id
            getPizRecipeWithDetails(id)
        }
    }

    sealed class UiEvent {
        data class ShowToast(val massage: String) : UiEvent()
        object NavigateBack: UiEvent()
    }

    // standard detail screen
    private fun getPizRecipeWithDetails(id: String) {
        // observe result
        getPizRecipeWithdetailsJob?.cancel()
        getPizRecipeWithdetailsJob = allPizRecipesUseCases.getPizRecipeWithDetailsFlowUseCase().onEach {
            _pizRecipeState.value = _pizRecipeState.value.copy(
                pizRecipe = it,
                imageBitmap = it?.let { allPizRecipesUseCases.getRecipeImageUseCase(it.imageName) },
                firstLaunch = true
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

    private fun clickEditIngredient(id: String, isStepIngredient: Boolean, stepId: String?) {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Ingredient)
        // load correct ingredient to edit ingredient state
        if (stepId == null && currentRecipeId == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val ingredient = allPizRecipesUseCases.getIngredientUseCase(id, currentRecipeId!!, stepId, isStepIngredient)
            if(ingredient != null) {
                withContext(Dispatchers.Main) {
                    _editIngredientState.value = _editIngredientState.value.copy(
                        id = id,
                        ingredientName = ingredient.ingredient,
                        ingredientAmount = ingredient.baseAmount.toString(),
                        isStepIngredient = isStepIngredient,
                        stepId = stepId,
                    )
                }
            } else {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowToast("Die Zutat wurde nicht gefunden."))
                }
            }
        }
    }

    private fun clickEditStep(id: String) {
        currentRecipeId?.let {
            _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Step)
            viewModelScope.launch(Dispatchers.IO) {
                val stepWithoutIngredients = allPizRecipesUseCases.getStepWithoutIngredientsUseCase(id, it)
                if (stepWithoutIngredients != null) {
                    withContext(Dispatchers.Main) {
                        _editStepState.value = _editStepState.value.copy(
                            id = id,
                            description = stepWithoutIngredients.description
                        )
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowToast("Der Schritt wurde nicht gefunden."))
                    }
                }
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
                onEvent(PizRecipeDetailEvent.DismissEditDialog)

            } catch (e: java.lang.NumberFormatException) {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowToast("Gib eine Kommazahl ein."))
                }
            }
        }
    }

    private fun saveImage() {
        val state = _editImageState.value
        val recipe = _pizRecipeState.value.pizRecipe
        viewModelScope.launch(Dispatchers.IO) {
            if (state.bitmap != null ) {
                if(recipe != null) {

                    // if recipe has no image name yet, set it to the recipe id
                    val imageName = if(state.imageName == "") recipe.id else state.imageName

                    // save image bitmap
                    allPizRecipesUseCases.saveRecipeImageUseCase(imageName, state.bitmap)

                    // save image ref to recipe
                    allPizRecipesUseCases.updateRecipeUseCase(
                        PizRecipe(recipe.title, recipe.feature, imageName, recipe.prepTime, recipe.id)
                    )

                    // reload data
                    _pizRecipeState.value = _pizRecipeState.value.copy(
                        imageBitmap = state.bitmap //allPizRecipesUseCases.getRecipeImageUseCase(imageName) // TODO currently not reloading from data source here
                    )
                } else {
                    _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut"))
                }
            } else {
                _eventFlow.emit(UiEvent.ShowToast("Wähle ein Bild aus oder bearbeite es um zu speichern."))
            }
        }
        // dismiss dialog
        onEvent(PizRecipeDetailEvent.DismissEditDialog)

    }

    private fun saveIngredient() {

        val state = _editIngredientState.value

        currentRecipeId?.also {
            try {
                val amount = state.ingredientAmount.toDouble()
                val pizIngredient = PizIngredient(
                    state.ingredientName, amount, state.id
                )

                viewModelScope.launch(Dispatchers.IO) {
                    // insert/update ingredient
                    allPizRecipesUseCases.updateIngredientUseCase.invoke(pizIngredient, it, state.stepId, state.isStepIngredient)

                    // reload data
                    allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
                }
                // dismiss dialog
                onEvent(PizRecipeDetailEvent.DismissEditDialog)

            } catch (e: java.lang.NumberFormatException) {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowToast("Gib eine Kommazahl ein."))
                }
            }
        } ?: run {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut."))
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
            onEvent(PizRecipeDetailEvent.DismissEditDialog)

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
    private fun editImage(bitmap: ImageBitmap) {
        _editImageState.value = _editImageState.value.copy(
            bitmap = bitmap
        )

//        Log.i("edit image", "saving three images now")
        // for testing: save full image, resized image and compressed image
//        viewModelScope.launch(Dispatchers.IO) {
//            allPizRecipesUseCases.saveRecipeImageUseCase(imageName = imageName + "full", imageBitmap = bitmap)

//            // testing resize and compression
//            // ratio
//            val width: Int = bitmap.width
//            val height: Int = bitmap.height
//            val ratioBitmap = width.toFloat() / height.toFloat()
//
//            val resizeWidth = 640
//            val resizeHeight = (resizeWidth / ratioBitmap).toInt()
//
//            // Resize the image to the desired resolution
//            val resizedBitmap = Bitmap.createScaledBitmap(bitmap.asAndroidBitmap(), resizeWidth, resizeHeight, true)
//            allPizRecipesUseCases.saveRecipeImageUseCase(imageName = urlOrWhatever + "resized", imageBitmap = resizedBitmap.asImageBitmap())
//
//            // Compress the resized image
//            val baos = ByteArrayOutputStream()
//            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
//            val imageBytes: ByteArray = baos.toByteArray()
//            val inputStream = ByteArrayInputStream(imageBytes)
//            val resizedCompressedBitmap = BitmapFactory.decodeStream(inputStream)
//            allPizRecipesUseCases.saveRecipeImageUseCase(imageName = urlOrWhatever + "resized_compressed", imageBitmap = resizedCompressedBitmap.asImageBitmap())

//        }
    }
    private fun editStepDescription(value: String) {
        _editStepState.value = _editStepState.value.copy(
            description = value
        )
    }

    // edit add/delete
    private fun editAddIngredient(isStepIngredient: Boolean, stepId: String?) {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Ingredient)
        // load correct ingredient to edit ingredient state
        if (isStepIngredient && currentRecipeId == null) {
            return
        }
        val mapId = if (isStepIngredient && stepId != null) stepId else currentRecipeId!!

        _editIngredientState.value = _editIngredientState.value.copy(
            id = UUID.randomUUID().toString(),
            ingredientName = "",
            ingredientAmount = "",
            isStepIngredient = isStepIngredient,
            stepId = mapId,
        )
    }

    private fun editAddStep() {
        _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.Step)
        // load correct step to edit ingredient state
        _editStepState.value = _editStepState.value.copy(
            id = UUID.randomUUID().toString(),
            description = ""
        )

    }

    private fun editDeleteRecipe(){
        currentRecipeId?.let {
            // delete
            viewModelScope.launch(Dispatchers.IO) {
                allPizRecipesUseCases.deleteRecipeUseCase(it)
                // navigate back // TODO async?
                _eventFlow.emit(UiEvent.NavigateBack)
            }
        } ?: run {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut."))
            }
        }
    }
    private fun editDeleteIngredient() {
        currentRecipeId?.let {
            // delete
            val state = _editIngredientState.value
            viewModelScope.launch(Dispatchers.IO) {
                allPizRecipesUseCases.deleteIngredientUseCase.invoke(
                    state.id,
                    it,
                    state.stepId,
                    state.isStepIngredient
                )
                // reload data
                allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
            }
            // dismiss dialogs
            onEvent(PizRecipeDetailEvent.DismissEditDialog)
            onEvent(PizRecipeDetailEvent.DismissDeleteDialog)
        } ?: run {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut."))
            }
        }
    }

    private fun editDeleteStep() {
        currentRecipeId?.let {
            // delete
            viewModelScope.launch(Dispatchers.IO) {
                allPizRecipesUseCases.deleteStepUseCase(
                    _editStepState.value.id,
                    it
                )
                // reload data
                allPizRecipesUseCases.loadPizRecipeWithDetailsUseCase(it)
            }
            // dismiss dialog
            onEvent(PizRecipeDetailEvent.DismissEditDialog)
            onEvent(PizRecipeDetailEvent.DismissDeleteDialog)
        } ?: run {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowToast("Unerwarteter Fehler. Bitte öffne das Rezept erneut."))
            }
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
            is PizRecipeDetailEvent.ImageChanged -> editImage(event.bitmap)
            is PizRecipeDetailEvent.StepDescriptionChanged -> editStepDescription(event.value)

            is PizRecipeDetailEvent.ClickAddIngredient -> editAddIngredient(
                event.isStepIngredient,
                event.stepId
            )
            PizRecipeDetailEvent.ClickAddStep -> editAddStep()

            PizRecipeDetailEvent.ClickDeleteRecipe -> editDeleteRecipe()
            PizRecipeDetailEvent.ClickDeleteIngredient -> editDeleteIngredient()
            PizRecipeDetailEvent.ClickDeleteStep -> editDeleteStep()

            PizRecipeDetailEvent.DismissEditDialog -> {
                _detailEditDialogState.value = _detailEditDialogState.value.copy(editDialogState = EditDialog.None)
            }
            is PizRecipeDetailEvent.ShowDeleteDialog -> {
                _detailDeleteDialogState.value = _detailDeleteDialogState.value.copy(deleteDialog = event.deleteDialog)
            }
            PizRecipeDetailEvent.DismissDeleteDialog -> {
                _detailDeleteDialogState.value = _detailDeleteDialogState.value.copy(deleteDialog = DeleteDialog.None)
            }
        }
    }

    private fun resetRecipeWithDetails() {
        allPizRecipesUseCases.resetPizRecipeWithDetailsFlowUseCase()
    }
}


