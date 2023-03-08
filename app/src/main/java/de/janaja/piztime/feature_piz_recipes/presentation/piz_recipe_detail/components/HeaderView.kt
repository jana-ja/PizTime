package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.presentation.util.TopSheetShape
import de.janaja.piztime.feature_piz_recipes.presentation.util.bottomElevation
import kotlinx.coroutines.launch

@Composable
fun HeaderView(
    modifier: Modifier = Modifier,
    title: String,
    feature: String,
    imageResId: Int,
    contentModifier: Modifier = Modifier,
    height: Dp,
    onClickEdit: () -> Unit // TODO implement UiEvent
) {

    // animation stuff
    val animDuration = 1100
    var screenVisible by remember {
        mutableStateOf(true) // TODO change back
    }
    val transition = updateTransition(targetState = screenVisible, null)

    val pizOffset by transition.animateFloat(
        transitionSpec = { tween(animDuration, easing = LinearOutSlowInEasing) },
        label = ""
    ) {
        if (it) 0f else -400f
    }
    val pizRotation by transition.animateFloat(
        transitionSpec = { tween(animDuration, easing = LinearOutSlowInEasing) },
        label = ""
    ) {
        if (it)
            0f
        else
            -360f
    }
    val coroutineScope = rememberCoroutineScope()

    SideEffect {
        coroutineScope.launch {
            screenVisible = true
        }
    }



    // content
    // TODO this is only here to fix current preview issue with resource dimen values
    val borderHeight: Dp = 100.dp //dimensionResource(id = R.dimen.topSheetBorderHeight).value // TODO look up how to turn to dp properly
    Surface(
        modifier = modifier.fillMaxWidth().bottomElevation(),
        shape = TopSheetShape(borderHeight.value),
        color = Color.White,
        shadowElevation = 8.dp
    ) {


        Box(modifier = contentModifier
            .padding(16.dp)
            .height(height)) {
            Column(
                modifier = Modifier.matchParentSize().padding(bottom = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = "Start x hours early",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // pizza image
            Box(
                modifier = Modifier
                    .offset(pizOffset.dp)
                    .align(Alignment.TopEnd)
                    //.padding(top = 16.dp, end = 8.dp, bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Image of $title",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(height)
                        .padding(8.dp)
                        .rotate(pizRotation)

                )
            }

            // edit button
            IconButton(
                onClick = onClickEdit,
                Modifier
                    .size(36.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Edit,
                    "edit recipe",
                    Modifier.fillMaxHeight()
                )

            }
        }
    }
}

@Preview
@Composable
fun HeaderViewPreview() {
    HeaderView(
        title = "Pizza",
        feature = "ist lecker",
        imageResId = R.drawable.bsp_piz,
        height = 200.dp
    ) {}
}