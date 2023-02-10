package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.use_case.PizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.PizRecipesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipesViewModel @Inject constructor(
    private val pizRecipesUseCases: PizRecipeUseCases
) : ViewModel() {

    private val _state = mutableStateOf(PizRecipesState())
    val state: State<PizRecipesState> = _state

    private var getPizRecipeJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            pizRecipesUseCases.initDbIfEmptyUseCase()
        }
        getPizRecipes()
    }

    fun onEvent(event: PizRecipesEvent) {
        when (event) {
            is PizRecipesEvent.ClickPizRecipe -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateToDetail)
                }
            }
        }
    }

    private fun getPizRecipes() {
        getPizRecipeJob?.cancel()
        getPizRecipeJob = pizRecipesUseCases.getAllPizRecipesUseCase()
            .onEach {
                _state.value = _state.value.copy(
                    pizRecipes = it
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent{
        object NavigateToDetail: UiEvent()
    }
}