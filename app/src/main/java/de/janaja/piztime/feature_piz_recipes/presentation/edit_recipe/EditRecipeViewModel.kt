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
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.EditPizIngredientsState
import de.janaja.piztime.feature_piz_recipes.domain.util.DetailPizStepsWithIngredientsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {


    // TODO clear this state when dialog is dismissed or reload everytime dialog is opened
    private val _pizIngredientsState = mutableStateOf(EditPizIngredientsState())
    val pizIngredientsState: State<EditPizIngredientsState> = _pizIngredientsState

    private val _pizStepsWithIngredientsState = mutableStateOf(DetailPizStepsWithIngredientsState())
    val pizStepsWithIngredientsState: State<DetailPizStepsWithIngredientsState> = _pizStepsWithIngredientsState

    private var currentRecipeId: Long? = null


    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            currentRecipeId = id

            getPizIngredients(id)
//            getPizStepsWithIngredients(id)
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
//
//    fun getPizStepsWithIngredients(id: Long) {
//        getPizStepsWithIngredientsJob?.cancel()
//        getPizStepsWithIngredientsJob = allPizRecipesUseCases.getStepsWithIngredientsUseCase(id)
//            .onEach {
//                // convert map to list here
//                val stepWithIngredientList =
//                    mutableListOf<Pair<PizStepEntity, List<PizStepIngredientEntity>>>()
//                for (key in it.keys) {
//                    val value = it[key]
//                    if (value != null) {
//                        stepWithIngredientList.add(Pair(key, value))
//                    } else {
//                        stepWithIngredientList.add(Pair(key, listOf()))
//                    }
//                }
//                _pizStepsWithIngredientsState.value = _pizStepsWithIngredientsState.value.copy(
//                    pizStepsWithIngredients = stepWithIngredientList
//                )
//            }
//            .launchIn(viewModelScope)
//    }

    fun onEvent(event: EditRecipeEvent) {
        when (event) {
            is EditRecipeEvent.AmountChanged -> changeIngredientAmountInState(event.index, event.value)
            EditRecipeEvent.ClickAdd -> addIngredientToState()
            is EditRecipeEvent.ClickRemove -> removeIngredientFromState(event.index)
            EditRecipeEvent.ClickSave -> saveIngredients()
            is EditRecipeEvent.NameChanged -> changeIngredientNameInState(event.index, event.value)
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

    fun changeIngredientNameInState(index: Int, value: String) {
        Log.i("Help", "list before: ${_pizIngredientsState.value.ingredientNames}")

        _pizIngredientsState.value = _pizIngredientsState.value.copy(
            ingredientNames = _pizIngredientsState.value.ingredientNames.mapIndexed { i, oldValue ->
                Log.i("Help", "index: $i")
                Log.i("Help", "oldvlaue: $oldValue")
                Log.i("Help", "new value: $value")
                if (i == index) value else oldValue
            }.toMutableStateList()
        )
        Log.i("Help", "list after: ${_pizIngredientsState.value.ingredientNames}")

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
}


