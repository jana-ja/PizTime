package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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

@Composable
fun EditHeaderView(
    modifier: Modifier = Modifier,
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    val state = viewModel.editInfoState.value
    EditHeaderViewContent(
        modifier = modifier,
        title = state.title,
        feature = state.feature,
        viewModel::onEvent
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditHeaderViewContent(
    modifier: Modifier = Modifier,
    title: String,
    feature: String,
    onEvent: (PizRecipeDetailEvent) -> Unit,
) {

    // content

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Infos bearbeiten",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
//                actions = {
//                    IconButton(
//                        onClick = { onEvent(PizRecipeDetailEvent.ClickDeleteRecipe) },
//                    ) {
//                        Icon(
//                            Icons.Default.Delete,
//                            "delete recipe",
//                            Modifier.fillMaxHeight()
//                        )
//                    }
//                }
            )

        },
        bottomBar = {
            Button(
                onClick = { onEvent(PizRecipeDetailEvent.ClickSaveInfo) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    "save ingredient",
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(end = 24.dp, start = 16.dp)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ) {

            TextField(
                value = title,
                onValueChange = { onEvent(PizRecipeDetailEvent.RecipeTitleChanged(it)) },
                maxLines = 1,
                modifier = Modifier
                    .padding(end = 8.dp),
                label = { Text(text = "Title")}
            )

            TextField(
                value = feature,
                onValueChange = { onEvent(PizRecipeDetailEvent.RecipeFeatureChanged(it)) },
                maxLines = 1,
                modifier = Modifier,
                label = { Text(text = "Feature")}
            )

        }
    }
}

@Preview
@Composable
fun EditHeaderViewPreview() {
    EditHeaderViewContent(
        title = "Pizza",
        feature = "ist lecker",
        onEvent = {},
    )
}