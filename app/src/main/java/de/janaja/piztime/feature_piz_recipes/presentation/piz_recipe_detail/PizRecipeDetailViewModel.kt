package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.DetailAmountState
import de.janaja.piztime.feature_piz_recipes.domain.util.DetailPizRecipeWithDetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipeDetailViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailAmountState = mutableStateOf(DetailAmountState())
    val detailAmountState: State<DetailAmountState> = _detailAmountState

    private val _pizRecipeState = mutableStateOf(DetailPizRecipeWithDetailsState())
    val pizRecipeState: State<DetailPizRecipeWithDetailsState> = _pizRecipeState

    private var currentRecipeId: Long? = null

    private var getPizRecipeWithdetailsJob: Job? = null


    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->
            currentRecipeId = id
            getPizRecipeWithDetails(id)
        }
    }

    fun getPizRecipeWithDetails(id: Long) {
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
    fun onEvent(event: PizRecipeDetailEvent) {
        when (event) {
            is PizRecipeDetailEvent.SetAmount -> {
                _detailAmountState.value = _detailAmountState.value.copy(
                    amount = event.amount
                )
            }
        }
    }

    fun increaseAmount() {
        _detailAmountState.value = _detailAmountState.value.copy(amount = _detailAmountState.value.amount + 1)
    }

    fun decreaseAmount() {
        if (_detailAmountState.value.amount < 1) return
        _detailAmountState.value = _detailAmountState.value.copy(amount = _detailAmountState.value.amount - 1)
    }
}


