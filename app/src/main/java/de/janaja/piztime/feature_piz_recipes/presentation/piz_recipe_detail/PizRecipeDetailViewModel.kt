package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepEntity
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizStepIngredientEntity
import de.janaja.piztime.feature_piz_recipes.data.mapper.toRecipe
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.DetailAmountState
import de.janaja.piztime.feature_piz_recipes.domain.util.DetailPizIngredientsState
import de.janaja.piztime.feature_piz_recipes.domain.util.DetailPizRecipeWithDetailsState
import de.janaja.piztime.feature_piz_recipes.domain.util.DetailPizStepsWithIngredientsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipeDetailViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailAmountState = mutableStateOf(DetailAmountState())
    val detailAmountState: State<DetailAmountState> = _detailAmountState

    private val _pizRecipeState = mutableStateOf(DetailPizRecipeWithDetailsState())
    val pizRecipeState: State<DetailPizRecipeWithDetailsState> = _pizRecipeState

    private val _pizIngredientsState = mutableStateOf(DetailPizIngredientsState())
    val pizIngredientsState: State<DetailPizIngredientsState> = _pizIngredientsState

    private val _pizStepsWithIngredientsState = mutableStateOf(DetailPizStepsWithIngredientsState())
    val pizStepsWithIngredientsState: State<DetailPizStepsWithIngredientsState> =
        _pizStepsWithIngredientsState

    private var getPizRecipeWithdetailsJob: Job? = null
//    private var getPizRecipeJob: Job? = null
//    private var getPizIngredientsJob: Job? = null
//    private var getPizStepsWithIngredientsJob: Job? = null

    init {
        savedStateHandle.get<Long>("pizRecipeId")?.let { id ->

            getPizRecipeWithDetails(id)
//            getPizRecipe(id)
//            getPizIngredients(id)
//            getPizStepsWithIngredients(id)
        }
    }
    fun getPizRecipeWithDetails(id: Long) {
        // observe result
        getPizRecipeWithdetailsJob?.cancel()
        getPizRecipeWithdetailsJob = allPizRecipesUseCases.getPizRecipeWithDetailsFlowUseCase()
            .onEach {
                it?.let {
                    _pizRecipeState.value = _pizRecipeState.value.copy(
                        pizRecipe = it
                    )
                }

            }
            .launchIn(viewModelScope)
        // load data
        viewModelScope.launch {
            allPizRecipesUseCases.getPizRecipeWithDetailsUseCase(id)
        }
    }

//    fun getPizRecipe(id: Long) {
//        getPizRecipeJob?.cancel()
//        getPizRecipeJob = allPizRecipesUseCases.getPizRecipeUseCase(id)
//            .onEach {
//                it?.let {
//                    _pizRecipeState.value = _pizRecipeState.value.copy(
//                        pizRecipe = it.toRecipe()
//                    )
//                }
//
//            }
//            .launchIn(viewModelScope)
//    }
//
//    fun getPizIngredients(id: Long) {
//        getPizIngredientsJob?.cancel()
//        getPizIngredientsJob = allPizRecipesUseCases.getIngredientsUseCase(id)
//            .onEach {
//                _pizIngredientsState.value = _pizIngredientsState.value.copy(
//                    pizIngredients = it
//                )
//            }
//            .launchIn(viewModelScope)
//    }
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

    // 1:41 er sagt mit textfields ist alles in einem state nicht gut weil dann alle views recomposed werden wenn man buchstabe eintippt.
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
        _detailAmountState.value =
            _detailAmountState.value.copy(amount = _detailAmountState.value.amount + 1)
    }

    fun decreaseAmount() {
        if (_detailAmountState.value.amount < 1)
            return
        _detailAmountState.value =
            _detailAmountState.value.copy(amount = _detailAmountState.value.amount - 1)
    }


}