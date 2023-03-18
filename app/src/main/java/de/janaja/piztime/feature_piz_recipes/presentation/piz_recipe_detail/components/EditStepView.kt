package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun EditStepView(
    modifier: Modifier = Modifier,
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    EditStepViewContent(
        modifier = modifier,
        description = viewModel.editStepState.value.description,
        viewModel::onEvent
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditStepViewContent(
    modifier: Modifier = Modifier,
    description: String,
    onEvent: (PizRecipeDetailEvent) -> Unit
) {

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Schritt bearbeiten",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(PizRecipeDetailEvent.ClickDeleteStep) },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "delete step",
                            Modifier.fillMaxHeight()
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { onEvent(PizRecipeDetailEvent.ClickSaveStep) },
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
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(end = 24.dp, start = 16.dp)
                .padding(vertical = 24.dp)
            //.background(Color.LightGray)
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TextField(
                value = description,
                onValueChange = { onEvent(PizRecipeDetailEvent.StepDescriptionChanged(it)) },
                modifier = Modifier.fillMaxHeight(),
                label = {Text("Beschreibung")}
            )


        }
    }
}


@Preview
@Composable
fun EditStepViewPreview() {
    EditStepViewContent(
        modifier = Modifier,
        description = DummyData.DummySteps.first().description,
        onEvent = {})
}