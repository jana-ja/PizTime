package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.PizRecipesEvent
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.PizRecipesViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginView(
    viewModel: PizRecipesViewModel = hiltViewModel(),
    logIn: () -> Unit
) {

    val state = viewModel.loginState.value
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "LogIn",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        },
        bottomBar = {
            Button(
                onClick = logIn,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    "add ingredient",
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(end = 24.dp, start = 16.dp)
                .padding(vertical = 24.dp)
            //.background(Color.LightGray)
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(PizRecipesEvent.EmailChanged(it)) },
                modifier = Modifier,
                label = { Text("E-Mail") },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
            )
            TextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(PizRecipesEvent.PasswordChanged(it)) },
                modifier = Modifier,
                label = { Text("Passwort") },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {keyboardController?.hide()}),
            )
        }
    }
}