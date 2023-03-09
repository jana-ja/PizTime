package de.janaja.piztime.feature_piz_recipes.presentation.edit_recipe

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.EditPizIngredientsState
import de.janaja.piztime.feature_piz_recipes.domain.util.EditPizStepsWithIngredientsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _pizIngredientsState = mutableStateOf(EditPizIngredientsState())
    val pizIngredientsState: State<EditPizIngredientsState> = _pizIngredientsState

    private val _pizStepsWithIngredientsState = mutableStateOf(EditPizStepsWithIngredientsState())
    val pizStepsWithIngredientsState: State<EditPizStepsWithIngredientsState> = _pizStepsWithIngredientsState

    private var currentRecipeId: Long? = null


    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            currentRecipeId = id
            getPizIngredients(id)
            getPizStepsWithIngredients(id)
        }
    }


    private fun getPizIngredients(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val ingredients = allPizRecipesUseCases.getIngredientsUseCase(id)
            withContext(Dispatchers.Main) {
                _pizIngredientsState.value = _pizIngredientsState.value.copy(
                    pizIngredients = ingredients,
                    ingredientNames = (ingredients.map { ingredient -> ingredient.ingredient }).toMutableStateList(),
                    ingredientAmounts = ingredients.map { ingredient -> ingredient.baseAmount }
                )

            }
        }
    }

    private fun getPizStepsWithIngredients(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val steps = allPizRecipesUseCases.getStepsWithIngredientsUseCase(id)
            withContext(Dispatchers.Main) {
                _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
                    stepsWithIngredients = steps,
                    stepDescriptions = (steps.map { stepWithIngredients -> stepWithIngredients.description }),
                    ingredients = (steps.map { stepWithIngredient -> stepWithIngredient.ingredients })
                )

            }
        }
    }


    private fun saveIngredients() {
        currentRecipeId?.let {
            val pizIngredients = _pizIngredientsState.value.pizIngredients.indices.map { i ->
                PizIngredient(
                    _pizIngredientsState.value.ingredientNames[i],
                    _pizIngredientsState.value.ingredientAmounts[i],
                    _pizIngredientsState.value.pizIngredients[i].id
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                allPizRecipesUseCases.updateIngredientsUseCase(pizIngredients, currentRecipeId!!)

                // reload data
                allPizRecipesUseCases.getPizRecipeWithDetailsUseCase(it)

                // TODO navigate back/close dialog

            }
        } // TODO error handling

    }

    private fun removeIngredientFromState(index: Int) {
        _pizIngredientsState.value = _pizIngredientsState.value.copy(
            pizIngredients = _pizIngredientsState.value.pizIngredients.filterIndexed { i, _ -> i != index },
            ingredientNames = _pizIngredientsState.value.ingredientNames.filterIndexed { i, _ -> i != index }
                .toMutableStateList(),
            ingredientAmounts = _pizIngredientsState.value.ingredientAmounts.filterIndexed { i, _ -> i != index }
        )
    }

    private fun addIngredientToState() {
        _pizIngredientsState.value = _pizIngredientsState.value.copy(
            pizIngredients = _pizIngredientsState.value.pizIngredients + PizIngredient("", 0.0),
            ingredientNames = (_pizIngredientsState.value.ingredientNames + "").toMutableStateList(),
            ingredientAmounts = _pizIngredientsState.value.ingredientAmounts + 0.0
        )
    }

    private fun addStepToState() {
        val list = _pizStepsWithIngredientsState.value.ingredients.toMutableList()
        list.add(listOf())
        _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
            stepsWithIngredients = _pizStepsWithIngredientsState.value.stepsWithIngredients + PizStepWithIngredients(
                "",
                listOf()
            ),
            stepDescriptions = _pizStepsWithIngredientsState.value.stepDescriptions + "",
            ingredients = list.toList() // + operator does not append an empty list to a List<List<T>>.
        )
    }

    private fun removeStepFromState(index: Int) {
        _pizStepsWithIngredientsState
        _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
            stepsWithIngredients = _pizStepsWithIngredientsState.value.stepsWithIngredients.filterIndexed { i, _ -> i != index },
            stepDescriptions = _pizStepsWithIngredientsState.value.stepDescriptions.filterIndexed { i, _ -> i != index }
                .toMutableStateList(),
            ingredients = _pizStepsWithIngredientsState.value.ingredients.filterIndexed { i, _ -> i != index }
        )
    }
    
    private fun changeIngredientNameInState(index: Int, value: String) {
        _pizIngredientsState.value = _pizIngredientsState.value.copy(
            ingredientNames = _pizIngredientsState.value.ingredientNames.mapIndexed { i, oldValue ->
                if (i == index) value else oldValue
            }.toMutableStateList()
        )
    }

    private fun changeIngredientAmountInState(index: Int, value: String) {
        try {
            val doubleValue = value.toDouble()
            _pizIngredientsState.value = _pizIngredientsState.value.copy(
                ingredientAmounts = _pizIngredientsState.value.ingredientAmounts.mapIndexed { i, oldValue -> if (i == index) doubleValue else oldValue }
            )
        } catch (e: java.lang.NumberFormatException) {
            Log.e("Viewmodel", "invalid input")
            // TODO handle
        }

    }

    private fun saveSteps() {
        currentRecipeId?.let {
            val pizSteps = _pizStepsWithIngredientsState.value.stepsWithIngredients.indices.map { i ->
                PizStepWithIngredients(
                    description = _pizStepsWithIngredientsState.value.stepDescriptions[i],
                    ingredients = _pizStepsWithIngredientsState.value.ingredients[i],
                    id = _pizStepsWithIngredientsState.value.stepsWithIngredients[i].id
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                allPizRecipesUseCases.updateStepsWithIngredientsUseCase(pizSteps, currentRecipeId!!)

                // reload data
                allPizRecipesUseCases.getPizRecipeWithDetailsUseCase(it)

                // TODO navigate back/close dialog

            }
        } // TODO error handling

    }

    private fun changeStepDescriptionInState(index: Int, value: String) {
        _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
            stepDescriptions = _pizStepsWithIngredientsState.value.stepDescriptions.mapIndexed { i, oldValue ->
                if (i == index) value else oldValue
            }.toMutableStateList()
        )
    }

    fun onEvent(event: EditRecipeEvent) {
        when (event) {
            is EditRecipeEvent.AmountChanged -> changeIngredientAmountInState(event.index, event.value)
            EditRecipeEvent.ClickAddIngredient -> addIngredientToState()
            is EditRecipeEvent.ClickRemoveIngredient -> removeIngredientFromState(event.index)
            EditRecipeEvent.ClickSaveIngredients -> saveIngredients()
            is EditRecipeEvent.NameChanged -> changeIngredientNameInState(event.index, event.value)
            is EditRecipeEvent.StepChanged -> changeStepDescriptionInState(event.index, event.value)
            EditRecipeEvent.ClickSaveSteps -> saveSteps()
            EditRecipeEvent.ClickAddStep -> addStepToState()
            is EditRecipeEvent.ClickRemoveStep -> removeStepFromState(event.index)
        }
    }

    fun reloadIngredients() {
        currentRecipeId?.let { getPizIngredients(currentRecipeId!!) }
    }

    fun reloadSteps() {
        currentRecipeId?.let { getPizStepsWithIngredients(currentRecipeId!!) }
    }
}


