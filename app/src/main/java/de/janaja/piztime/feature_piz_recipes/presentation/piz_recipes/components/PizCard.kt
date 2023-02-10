package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.ui.theme.PizTimeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizCard(
    pizRecipe: PizRecipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp
) {
    Card(onClick = onClick) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {

            Text(text = pizRecipe.title, style = MaterialTheme.typography.titleMedium)
            Image(
                painter = painterResource(id = pizRecipe.imageResourceId),
                contentDescription = "Image of ${pizRecipe.title}",//stringResource(id = ),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = pizRecipe.feature,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.End)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PizCardPreview() {
    PizTimeTheme {
        PizCard(PizRecipe("Beste Piz", "Einfach nice", "Backen!", R.drawable.bsp_piz), { })
    }
}