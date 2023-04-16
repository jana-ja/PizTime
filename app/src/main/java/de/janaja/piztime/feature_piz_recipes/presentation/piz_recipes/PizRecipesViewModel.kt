package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.auth.AuthService
import de.janaja.piztime.feature_piz_recipes.domain.use_case.AllPizRecipeUseCases
import de.janaja.piztime.feature_piz_recipes.domain.util.HomeLogInDialogState
import de.janaja.piztime.feature_piz_recipes.domain.util.HomeLogInState
import de.janaja.piztime.feature_piz_recipes.domain.util.HomePizRecipesState
import de.janaja.piztime.feature_piz_recipes.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// in clean architecture viewmodel calls use cases or changes things in state (that ui observes)
@HiltViewModel
class PizRecipesViewModel @Inject constructor(
    private val allPizRecipesUseCases: AllPizRecipeUseCases,
    private val authService: AuthService
) : ViewModel() {

    private val _state = mutableStateOf(HomePizRecipesState())
    val state: State<HomePizRecipesState> = _state

    private val _loginState = mutableStateOf(HomeLogInState())
    val loginState: State<HomeLogInState> = _loginState

    private val _loginDialogState = mutableStateOf(HomeLogInDialogState())
    val loginDialogState: State<HomeLogInDialogState> = _loginDialogState

    val hasUser: State<Boolean> = authService.hasUser

    // ui event state
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getPizRecipesJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allPizRecipesUseCases.initDbIfEmptyUseCase()
        }
        observePizRecipes()
//        loadPizRecipes()
    }

    sealed class UiEvent {
        data class ShowToast(val massage: String) : UiEvent()
        data class Navigate(val recipeId: String): UiEvent()
    }

    // data
    private fun observePizRecipes() {
        getPizRecipesJob?.cancel()
        getPizRecipesJob = allPizRecipesUseCases.getAllPizRecipesFlowUseCase()
            .onEach {
                // TODO wenn empty oder null oder so loading anzeigen?
                _state.value = _state.value.copy(
                    pizRecipes = it,
                    recipeImages = it.map { recipe -> allPizRecipesUseCases.getRecipeImageUseCase(recipe.imageName) }
                )
            }
            .launchIn(viewModelScope)
    }

    fun loadPizRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            allPizRecipesUseCases.loadAllPizRecipesUseCase()
        }
    }

    // events

    private fun logIn() {
        val state = loginState.value
        if (state.email != "" && state.password != "") {
            viewModelScope.launch(Dispatchers.IO) {
                authService.logIn(state.email, state.password).collect{ result ->
                    when(result){
                        is Resource.Error -> {
                            viewModelScope.launch {
                                _eventFlow.emit(UiEvent.ShowToast(result.message?:"Fehler beim Einloggen"))
                            }
                        }
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            viewModelScope.launch {
                                _eventFlow.emit(UiEvent.ShowToast("Erfolgreich eingeloggt"))
                            }
                            dismissDialog()
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowToast("Gib eine E-Mail Adresse und ein Passwort ein."))
            }
        }
    }

    private fun logOut() {
        // TODO handle loading?
        // TODO which resource data type?
        viewModelScope.launch(Dispatchers.IO) {
            authService.logOut().collect{ result ->
                when(result){
                    is Resource.Error -> {
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.ShowToast(result.message?:"Fehler beim Ausloggen"))
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {}
                }
            }
        }
    }

    private fun editEmail(value: String) {
        _loginState.value = _loginState.value.copy(
            email = value
        )
    }

    private fun editPassword(value: String) {
        _loginState.value = _loginState.value.copy(
            password = value
        )
    }

    private fun dismissDialog() {
        _loginDialogState.value = _loginDialogState.value.copy(
            show = false
        )
        // clear login state
        _loginState.value = _loginState.value.copy(
            email = "",
            password = ""
        )
    }

    private fun showDialog() {
        _loginDialogState.value = _loginDialogState.value.copy(
            show = true
        )
    }

    private fun newRecipe(){
        viewModelScope.launch(Dispatchers.IO) {
            val newId = allPizRecipesUseCases.addPizRecipeUseCase()
            _eventFlow.emit(UiEvent.Navigate(newId))
        }
    }

    // handle events
    fun onEvent(event: PizRecipesEvent) {
        when (event) {
            is PizRecipesEvent.EmailChanged -> editEmail(event.value)
            is PizRecipesEvent.PasswordChanged -> editPassword(event.value)
            PizRecipesEvent.DismissDialog -> dismissDialog()
            PizRecipesEvent.ShowDialog -> showDialog()
            PizRecipesEvent.LogIn -> logIn()
            PizRecipesEvent.LogOut -> logOut()
            PizRecipesEvent.NewRecipe -> newRecipe()
        }
    }

}