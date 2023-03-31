package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.*
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import com.smarttoolfactory.cropper.settings.CropType
import de.janaja.piztime.R
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditImageView(
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    val state = viewModel.editImageState.value
    val context = LocalContext.current

    // get current image bitmap from state, if null show default image
    val bitmap: ImageBitmap =
        state.bitmap
            ?: ImageBitmap.imageResource(
                LocalContext.current.resources,
                R.drawable.bsp_piz
            )

//    val bitmap = remember {
//        mutableStateOf(currentImageBitmap)
//    }

    // select image
    val imageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
        // if new image gets selected then save its bitmap to bitmap state var
        if (imageUri.value != null) {
            val newBitmap =
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri.value).asImageBitmap()

                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, imageUri.value!!)
                    ImageDecoder.decodeBitmap(source).asImageBitmap()
                }

            // resize image
            val inputRatio = newBitmap.width.toFloat() / newBitmap.height.toFloat()
            val smallerSide = 860
            val resizeWidth: Int
            val resizeHeight: Int
            if(newBitmap.width < newBitmap.height){
                // portrait - ratio < 1
                resizeWidth = smallerSide
                resizeHeight = (resizeWidth / inputRatio).toInt()
            } else {
                // landscape - ratio >= 1
                resizeHeight = smallerSide
                resizeWidth = (inputRatio * resizeHeight).toInt()
            }
            // Resize the image to the desired resolution
            val resizedBitmap = Bitmap.createScaledBitmap(newBitmap.asAndroidBitmap(), resizeWidth, resizeHeight, true).asImageBitmap()

            // update image state
            viewModel.onEvent(
                PizRecipeDetailEvent.ImageChanged(
                    resizedBitmap,
                    state.imageName
                )
            )
        }
    }

    // crop image
    var isCropDialogShown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Bild bearbeiten",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        },
        bottomBar = {
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                FilledTonalButton(
                    onClick = {
                        // open image picker
                        launcher.launch("image/*")
                    },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Select")
                }

                FilledTonalButton(
                    onClick = {
                        // open crop dialog
                        isCropDialogShown = true
                    },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                ) {
                    Text("Crop")
                }

                Spacer(modifier = Modifier.weight(1.0f))

                Button(
                    onClick = { viewModel.onEvent(PizRecipeDetailEvent.ClickSaveImage) },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        "add ingredient",
                    )
                }
            }
        }
    ) { paddingValues ->

        // show fullscreen crop dialog
        if (isCropDialogShown) {
            Dialog(
                onDismissRequest = { isCropDialogShown = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                CropImageDialog(
                    currentImage = bitmap,
                    onCompletion = { croppedBitmap ->
                        viewModel.onEvent(
                            PizRecipeDetailEvent.ImageChanged(
                                croppedBitmap,
                                state.imageName
                            )
                        ); isCropDialogShown = false
                    },
                    onDismiss = { isCropDialogShown = false })
            }
        }

        // edit image view content
        Image(
            bitmap = bitmap.asAndroidBitmap().asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .size(400.dp)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CropImageDialog(
    currentImage: ImageBitmap,
    onCompletion: (bitmap: ImageBitmap) -> Unit,
    onDismiss: () -> Unit
) {

    val imageBitmap by remember { mutableStateOf(currentImage) }
    // TODO maybe change style?
//    var cropStyleInit = CropDefaults.style().copy(
//        backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
//    )
    val cropStyle by remember { mutableStateOf(CropDefaults.style()) }
    // crop circle shape
    val cropProperties by remember {
        mutableStateOf(
            CropDefaults.properties(
                cropType = CropType.Static,
                cropOutlineProperty = CropOutlineProperty(
                    OutlineType.Oval,
                    OvalCropShape(id = 0, title = "Oval")
                ),
                aspectRatio = AspectRatio(1 / 1f),
                handleSize = 20.dp
            )
        )
    }
    var crop by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Bild zuschneiden",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                FilledTonalButton(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        "cancel crop",
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Button(
                    onClick = { crop = true },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        "apply crop",
                    )
                }
            }
        }
    ) { paddingValues ->

        ImageCropper(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            imageBitmap = imageBitmap,
            contentDescription = "Image Cropper",
            cropStyle = cropStyle,
            cropProperties = cropProperties,
            crop = crop,
            onCropStart = {}
        ) {
            onCompletion(it)
        }
    }
}