package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.ui.theme.PizTimeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizCard(
    pizRecipe: PizRecipe,
    onClick: () -> Unit,
    index: Int = 0,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp
) {
    Card(onClick = onClick) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {


            var isVisible by remember {
                mutableStateOf(false)
            }

            val transition = updateTransition(targetState = isVisible, null)

            val pizRotation by transition.animateFloat(
                transitionSpec = {tween(1000)},
                label = ""
            ){
                if (it) 360f else 0f
            }
            val pizOffset by transition.animateFloat(
                transitionSpec = {tween(1000)},
                label = ""
            ){
                if (it) 0f else -300f
            }

//            AnimatedVisibility(visible = isVisible) {
                Text(text = pizRecipe.title, style = MaterialTheme.typography.titleMedium)
//            }
            Box(modifier = Modifier.offset(pizOffset.dp).align(Alignment.CenterHorizontally)) {
                Image(
                    painter = painterResource(id = pizRecipe.imageResourceId),
                    contentDescription = "Image of ${pizRecipe.title}",//stringResource(id = ),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)

                        .rotate(pizRotation)

                )
            }

//            AnimatedVisibility(visible = isVisible) {
                Text(
                    text = pizRecipe.feature,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.End)
                )
//            }

            val coroutineScope = rememberCoroutineScope()

            SideEffect {
                coroutineScope.launch() {
                    delay((index * 300).toLong())
                    isVisible = true
                }
            }
            
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