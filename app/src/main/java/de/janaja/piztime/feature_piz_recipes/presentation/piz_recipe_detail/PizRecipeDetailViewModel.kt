package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.use_case.PizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.PizRecipeDetailState
import de.janaja.piztime.feature_piz_recipes.domain.util.PizRecipesState
import kotlinx.coroutines.launch
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipeDetailViewModel @Inject constructor(
    private val pizRecipesUseCases: PizRecipeUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _state = mutableStateOf(PizRecipeDetailState())
    val state : State<PizRecipeDetailState> = _state


    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            viewModelScope.launch {
                pizRecipesUseCases.getPizRecipeUseCase.invoke(id)?.also {
                    _state.value = _state.value.copy(pizRecipe =  it)
                }
            }
        }
    }

    // 1:41 er sagt mit textfields ist alles in einem state nicht gut weil dann alle views recomposed werden wenn man buchstabe eintippt.
    fun onEvent(event: PizRecipeDetailEvent){
        when(event){
            is PizRecipeDetailEvent.SetAmount -> {
                _state.value = _state.value.copy(
                    amount = event.amount
                )
            }
        }
    }


}