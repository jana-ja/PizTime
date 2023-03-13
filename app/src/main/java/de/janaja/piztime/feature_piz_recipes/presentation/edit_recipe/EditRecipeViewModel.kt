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
                    stepIngredients = (steps.map { stepWithIngredient -> stepWithIngredient.ingredients }),
                    stepIngredientsNames = (steps.map { stepWithIngredient -> stepWithIngredient.ingredients.map { ingredient -> ingredient.ingredient } }),
                    stepIngredientsAmounts = (steps.map { stepWithIngredient -> stepWithIngredient.ingredients.map { ingredient -> ingredient.baseAmount } })
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
        val ingredients = _pizStepsWithIngredientsState.value.stepIngredients.toMutableList()
        ingredients.add(listOf())
        val ingredientsNames = _pizStepsWithIngredientsState.value.stepIngredientsNames.toMutableList()
        ingredientsNames.add(listOf())
        val ingredientsAmounts = _pizStepsWithIngredientsState.value.stepIngredientsAmounts.toMutableList()
        ingredientsAmounts.add(listOf())
        _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
            stepsWithIngredients = _pizStepsWithIngredientsState.value.stepsWithIngredients + PizStepWithIngredients(
                "",
                listOf()
            ),
            stepDescriptions = _pizStepsWithIngredientsState.value.stepDescriptions + "",
            stepIngredients = ingredients.toList(),
            stepIngredientsNames = ingredientsNames.toList(),
            stepIngredientsAmounts = ingredientsAmounts.toList() // + operator does not append an empty list to a List<List<T>>.
        )
    }

    private fun removeStepFromState(index: Int) {
        _pizStepsWithIngredientsState
        _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
            stepsWithIngredients = _pizStepsWithIngredientsState.value.stepsWithIngredients.filterIndexed { i, _ -> i != index },
            stepDescriptions = _pizStepsWithIngredientsState.value.stepDescriptions.filterIndexed { i, _ -> i != index }
                .toMutableStateList(),
            stepIngredients = _pizStepsWithIngredientsState.value.stepIngredients.filterIndexed { i, _ -> i != index },
            stepIngredientsNames = _pizStepsWithIngredientsState.value.stepIngredientsNames.filterIndexed { i, _ -> i != index },
            stepIngredientsAmounts = _pizStepsWithIngredientsState.value.stepIngredientsAmounts.filterIndexed { i, _ -> i != index }
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
            val state = _pizStepsWithIngredientsState.value
            val pizSteps = state.stepsWithIngredients.indices.map { i ->
                PizStepWithIngredients(
                    description = state.stepDescriptions[i],
                    ingredients = state.stepIngredientsNames[i].indices.map { index ->
                        PizIngredient(
                            state.stepIngredientsNames[i][index],
                            state.stepIngredientsAmounts[i][index],
                            state.stepIngredients[i][index].id
                        )
                    },
                    id = state.stepsWithIngredients[i].id
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

    private fun changeStepIngredientAmountInState(stepIndex: Int, index: Int, value: String) {
        // TODO cant remove the decimal dot in text field with this method
        try {
            val valueInDouble = value.toDouble()
            _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
                stepIngredientsAmounts = _pizStepsWithIngredientsState.value.stepIngredientsAmounts.mapIndexed { i, oldValue ->
                    if (i == stepIndex) oldValue.mapIndexed { i2, oldValue2 ->
                        if (i2 == index) valueInDouble else oldValue2
                    } else oldValue
                }.toMutableStateList()
            )
        } catch (e: java.lang.NumberFormatException){
            // TODO
        }

    }

    private fun changeStepIngredientNameInState(stepIndex: Int, index: Int, value: String) {
        _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
            stepIngredientsNames = _pizStepsWithIngredientsState.value.stepIngredientsNames.mapIndexed { i, oldValue ->
                if (i == stepIndex) oldValue.mapIndexed { i2, oldValue2 ->
                    if (i2 == index) value else oldValue2
                } else oldValue
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
            is EditRecipeEvent.StepIngredientNameChanged -> changeStepIngredientNameInState(
                event.stepIndex,
                event.index,
                event.value
            )
            is EditRecipeEvent.StepIngredientAmountChanged -> changeStepIngredientAmountInState(
                event.stepIndex,
                event.index,
                event.value
            )
        }
    }


    fun reloadIngredients() {
        currentRecipeId?.let { getPizIngredients(currentRecipeId!!) }
    }

    fun reloadSteps() {
        currentRecipeId?.let { getPizStepsWithIngredients(currentRecipeId!!) }
    }
}


