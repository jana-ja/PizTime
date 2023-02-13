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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
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
    index: Int = 0
) {

    // animation stuff
    val animDuration = 900
    val animDelay: Long = 300

    // card animation stuff
    var cardVisible by remember {
        mutableStateOf(false)
    }
    val transition2 = updateTransition(targetState = cardVisible, null)
    val alpha by transition2.animateFloat(
        transitionSpec = {tween(animDuration)},
        label = ""
    ){
        if (it) 1f else 0f
    }
    val mtColor = MaterialTheme.colorScheme.surfaceVariant
    val cardColor = Color(mtColor.red, mtColor.green, mtColor.blue, alpha)


    // piz animation stuff
    var pizVisible by remember {
        mutableStateOf(false)
    }
    val transition = updateTransition(targetState = pizVisible, null)
    val pizRotation by transition.animateFloat(
        transitionSpec = {tween(animDuration)},
        label = ""
    ){
        if (it) 360f else 0f
    }
    val pizOffset by transition.animateFloat(
        transitionSpec = {tween(animDuration)},
        label = ""
    ){
        if (it) 0f else -300f
    }

    // content
    Card(onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor =  cardColor
        ),
    ) {

        val coroutineScope = rememberCoroutineScope()
        SideEffect {
            coroutineScope.launch() {
                delay((index * animDelay))
                pizVisible = true
                delay(animDuration.toLong())
                cardVisible = true
            }
        }

        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {


//            AnimatedVisibility(visible = isVisible) {
                Text(text = pizRecipe.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.alpha(alpha))
//            }
            Box(modifier = Modifier
                .offset(pizOffset.dp)
                .align(Alignment.CenterHorizontally)) {
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
                    modifier = Modifier.align(Alignment.End).alpha(alpha)
                )
//            }



            
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