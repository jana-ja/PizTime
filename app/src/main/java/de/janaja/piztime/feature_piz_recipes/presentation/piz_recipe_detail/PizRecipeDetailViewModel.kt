package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.use_case.PizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.PizAmountState
import de.janaja.piztime.feature_piz_recipes.domain.util.PizRecipeDetailState
import kotlinx.coroutines.launch
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipeDetailViewModel @Inject constructor(
    private val pizRecipesUseCases: PizRecipeUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _pizRecipeState = mutableStateOf(PizRecipeDetailState())
    val pizRecipeState : State<PizRecipeDetailState> = _pizRecipeState

    private val _pizAmountState = mutableStateOf(PizAmountState())
    val pizAmountState : State<PizAmountState> = _pizAmountState
    
    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            viewModelScope.launch {
                pizRecipesUseCases.getPizRecipeUseCase.invoke(id)?.also {
                    _pizRecipeState.value = _pizRecipeState.value.copy(pizRecipe =  it.first, pizIngredients = it.second)
                }
            }
        }
    }

    // 1:41 er sagt mit textfields ist alles in einem state nicht gut weil dann alle views recomposed werden wenn man buchstabe eintippt.
    fun onEvent(event: PizRecipeDetailEvent){
        when(event){
            is PizRecipeDetailEvent.SetAmount -> {
                _pizAmountState.value = _pizAmountState.value.copy(
                    amount = event.amount
                )
            }
        }
    }

    fun increaseAmount(){
        _pizAmountState.value = _pizAmountState.value.copy(amount = _pizAmountState.value.amount + 1)
    }

    fun decreaseAmount(){
        if (_pizAmountState.value.amount < 1)
            return
        _pizAmountState.value = _pizAmountState.value.copy(amount = _pizAmountState.value.amount - 1)
    }


}