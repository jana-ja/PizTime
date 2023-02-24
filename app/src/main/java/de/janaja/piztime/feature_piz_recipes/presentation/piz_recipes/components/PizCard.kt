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
import de.janaja.piztime.feature_piz_recipes.data.local.model.PizRecipeEntity
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.ui.theme.PizTimeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PizVisibility {
    ENTER, VISIBLE, EXIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizCard(
    pizRecipeEntity: PizRecipe,
    onClick: () -> Unit,
    index: Int
) {

    // animation stuff
    val animDuration = 900
    val animDelay: Long = 400

    // card animation stuff
    var cardVisible by remember {
        mutableStateOf(false)
    }
    val transition2 = updateTransition(targetState = cardVisible, null)
    val alpha by transition2.animateFloat(
        transitionSpec = { tween(animDuration) },
        label = ""
    ) {
        if (it) 1f else 0f
    }
    val mtColor = MaterialTheme.colorScheme.surfaceVariant
    val cardColor = Color(mtColor.red, mtColor.green, mtColor.blue, alpha)


    // piz animation stuff

    var pizVisible: PizVisibility by remember {
        mutableStateOf(PizVisibility.ENTER)
    }
    val transition = updateTransition(targetState = pizVisible, null)
    val pizRotation by transition.animateFloat(
        transitionSpec = { tween(animDuration) },
        label = ""
    ) {
        when (it) {
            PizVisibility.ENTER -> -360f
            PizVisibility.VISIBLE -> 0f
            PizVisibility.EXIT -> 360f
        }
    }
    val pizOffset by transition.animateFloat(
        transitionSpec = { tween(animDuration) },
        label = ""
    ) {
        when (it) {
            PizVisibility.ENTER -> -300f
            PizVisibility.VISIBLE -> 0f
            PizVisibility.EXIT -> 300f
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // content
    Card(
        onClick = {
            coroutineScope.launch {
                cardVisible = false
                delay((animDuration / 2).toLong())
                pizVisible = PizVisibility.EXIT
                delay(animDuration.toLong())
                onClick.invoke()
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {

        SideEffect {
            if (pizVisible == PizVisibility.ENTER) {
                coroutineScope.launch {
                    delay((index * animDelay))
                    pizVisible = PizVisibility.VISIBLE
                    delay((animDuration / 2).toLong())
                    cardVisible = true
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {


//            AnimatedVisibility(visible = isVisible) {
            Text(
                text = pizRecipeEntity.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.alpha(alpha)
            )
//            }
            Box(
                modifier = Modifier
                    .offset(pizOffset.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = pizRecipeEntity.imageResourceId),
                    contentDescription = "Image of ${pizRecipeEntity.title}",//stringResource(id = ),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)

                        .rotate(pizRotation)

                )
            }

//            AnimatedVisibility(visible = isVisible) {
            Text(
                text = pizRecipeEntity.feature,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .align(Alignment.End)
                    .alpha(alpha)
            )
//            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun PizCardPreview() {
    PizTimeTheme {
        PizCard(DummyData.DummyPizRecipe, {}, 0)
    }
}