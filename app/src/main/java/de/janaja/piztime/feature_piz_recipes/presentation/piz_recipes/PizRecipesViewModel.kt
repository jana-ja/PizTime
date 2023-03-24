package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.HomePizRecipesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipesViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HomePizRecipesState())
    val state: State<HomePizRecipesState> = _state

    private var getPizRecipesJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allPizRecipesUseCases.initDbIfEmptyUseCase()
        }
        observePizRecipes()
//        loadPizRecipes()
    }


    private fun observePizRecipes() {
        getPizRecipesJob?.cancel()
        getPizRecipesJob = allPizRecipesUseCases.getAllPizRecipesFlowUseCase()
            .onEach {
                _state.value = _state.value.copy(
                    pizRecipes = it,
                    recipeImages = it.map { recipe -> allPizRecipesUseCases.getRecipeImageUseCase(recipe.imageName) }
                )
            }
            .launchIn(viewModelScope)
    }

    fun loadPizRecipes(){
        viewModelScope.launch(Dispatchers.IO) {
            allPizRecipesUseCases.loadAllPizRecipesUseCase()
        }
    }



}