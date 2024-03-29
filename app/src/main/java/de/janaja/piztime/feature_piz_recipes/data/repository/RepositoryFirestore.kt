package de.janaja.piztime.feature_piz_recipes.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.janaja.piztime.feature_piz_recipes.data.mapper.*
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData
import de.janaja.piztime.feature_piz_recipes.presentation.util.TAG
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream


class RepositoryFirestore : Repository {

    private val recipesPath = "recipes"
    private val ingredientsPath = "ingredients"
    private val stepsPath = "steps"
    private val imagePath = "recipeImages"


    private val db = Firebase.firestore // TODO dependency inject?
    private val storage = Firebase.storage


    private val _pizRecipeWithDetailsFlow: MutableStateFlow<PizRecipeWithDetails?> = MutableStateFlow(null)
    override val pizRecipeWithDetailsFlow = _pizRecipeWithDetailsFlow.asStateFlow()

    private val _allPizRecipesFlow: MutableStateFlow<List<PizRecipe>> = MutableStateFlow(listOf())
    override val allPizRecipesFlow = _allPizRecipesFlow.asStateFlow()

    override suspend fun initDbIfEmpty() {
        // executed this once, don't need to do it again
        return


        DummyData.DummyRecipeWithDetailsData.forEach { recipe ->
            // insert piz recipe
            val recipesRef = db.collection(recipesPath)
            recipesRef.document(recipe.id)
                .set(recipe.toHashMap())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            // insert ingredients
            insertPizIngredients(recipe.ingredients, recipe.id)

            // insert steps with ingredients
            recipe.steps.forEach { stepWithIngredients ->
                insertPizStep(stepWithIngredients, recipe.id)
                insertPizStepIngredients(stepWithIngredients.ingredients, recipe.id, stepWithIngredients.id)
            }
        }
        loadAllPizRecipes()
    }

    // load
    override suspend fun loadAllPizRecipes() {
        // TODO add toast or something to show when data is loaded from cache
        db.collection(recipesPath)
            .get()
            .addOnSuccessListener { result ->
                val recipes = mutableListOf<PizRecipe>()
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    try {
                        recipes.add(
                            document.toPizRecipe()
                        )
                    } catch (e: java.lang.NullPointerException) {
                        firestoreWrongFormatException(e)
                    }
                }
                _allPizRecipesFlow.update { recipes }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    override suspend fun loadPizRecipeWithDetails(id: String) {
        val documentRef = db.collection(recipesPath).document(id)


        // ingredients
        val ingredientsCollection = documentRef.collection(ingredientsPath)
        val ingredients = mutableListOf<PizIngredient>()
        val ingredientResult = ingredientsCollection.get().await()
        for (ingredientDocument in ingredientResult) {
            ingredients.add(
                ingredientDocument.toPizIngredient()
            )
        }

        // steps
        val stepsCollection = documentRef.collection(stepsPath)
        val steps = mutableListOf<PizStepWithIngredients>()
        val stepResult = stepsCollection.get().await()
        for (stepDocument in stepResult) {
            // step ingredients
            val stepIngredientsCollection =
                documentRef.collection(stepsPath).document(stepDocument.id).collection(ingredientsPath)
            val stepIngredients = mutableListOf<PizIngredient>()
            val stepIngredientsResult = stepIngredientsCollection.get().await()
            for (ingredientDocument in stepIngredientsResult) {
                stepIngredients.add(
                    ingredientDocument.toPizIngredient()
                )
            }
            steps.add(
                stepDocument.toPizStepWithIngredients(stepIngredients)
            )
        }

        // recipe
        val recipeDocument = documentRef.get().await()
        val data = recipeDocument.data
        if (data != null) {
            _pizRecipeWithDetailsFlow.update {
                recipeDocument.toPizRecipeWithDetails(ingredients, steps)
            }
        }
    }

    override fun resetRecipeWithDetailsFlow() {
        _pizRecipeWithDetailsFlow.update { null }
    }

    override suspend fun getPizRecipe(id: String): PizRecipe? {
        val document = db.collection(recipesPath).document(id).get().await()
        val data = document.data
        return if (data != null) {
            try {
                document.toPizRecipe()
            } catch (e: java.lang.NullPointerException) {
                firestoreWrongFormatException(e)
                null
            }
        } else {
            null
        }
    }

    override suspend fun getRecipeImage(imageName: String): ImageBitmap? {

        if (imageName == "")
            return null

        val storageRef = storage.reference
        val recipeImgRef = storageRef.child("$imagePath/$imageName.png")
        // TODO try if exists
        val ONE_MEGABYTE: Long = 1024 * 1024 // TODO enough?
        val byteArray: ByteArray? = recipeImgRef.getBytes(ONE_MEGABYTE).await()
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            return bitmap.asImageBitmap()
        }
//            .addOnSuccessListener {
//            // Data for "images/island.jpg" is returned, use this as needed
//            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
//            return bitmap.asImageBitmap()
//        }.addOnFailureListener {
//// catch not found excpetion
//            // Handle any errors
//        }
        return null
    }

    override suspend fun getPizIngredient(id: String, recipeId: String): PizIngredient? {
        val document =
            db.collection(recipesPath).document(recipeId).collection(ingredientsPath).document(id).get().await()
        val data = document.data
        return if (data != null) {
            try {
                document.toPizIngredient()
                PizIngredient(
                    ingredient = data["ingredient"].toString(),
                    baseAmount = data["baseAmount"].toString().toDouble(),
                    id = document.id
                )
            } catch (e: java.lang.NullPointerException) {
                firestoreWrongFormatException(e)
                null
            }
        } else {
            null
        }
    }

    override suspend fun getPizStepWithoutIngredients(id: String, recipeId: String): PizStepWithIngredients? {
        val stepDocument =
            db.collection(recipesPath).document(recipeId).collection(stepsPath).document(id).get().await()
        return if (stepDocument.data != null) {
            try {
                stepDocument.toPizStepWithoutIngredients()
            } catch (e: java.lang.NullPointerException) {
                firestoreWrongFormatException(e)
                null
            }
        } else {
            null
        }
    }

    override suspend fun getPizStepIngredient(id: String, recipeId: String, stepId: String): PizIngredient? {
        val document =
            db.collection(recipesPath).document(recipeId).collection(stepsPath).document(stepId)
                .collection(ingredientsPath).document(id).get().await()
        val data = document.data
        return if (data != null) {
            try {
                document.toPizIngredient()
            } catch (e: java.lang.NullPointerException) {
                firestoreWrongFormatException(e)
                null
            }
        } else {
            null
        }
    }

    override suspend fun deletePizRecipeWithDetails(recipeId: String) {
        val recipeRef = db.collection(recipesPath).document(recipeId)
        // ingredients
        deletePizIngredientsForRecipeId(recipeId)
        // steps with step ingredients
        val stepsCollection = recipeRef.collection(stepsPath)
        val stepResult = stepsCollection.get().await()
        for (stepDocument in stepResult) {
            deletePizStepWithIngredients(stepDocument.id, recipeId)
        }
        // recipe
        recipeRef
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        // image
        deletePizRecipeImage(recipeId)
    }

    override suspend fun deletePizRecipeImage(imageName: String){

        val storageRef = storage.reference

        val recipeImgRef = storageRef.child("$imagePath/$imageName.png")
        recipeImgRef.delete()
            .addOnFailureListener {
                // TODO
            }.addOnSuccessListener {
                // TODO
            }
    }

    override suspend fun deletePizIngredientsForRecipeId(recipeId: String) {
        val ingredientCollection = db.collection(recipesPath).document(recipeId).collection(ingredientsPath)
        val ingredientResult = ingredientCollection.get().await()
        for (ingredientDocument in ingredientResult) {
            db.collection(recipesPath).document(recipeId).collection(ingredientsPath).document(ingredientDocument.id)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }
    }

    override suspend fun deletePizStepIngredientsForStepId(stepId: String, recipeId: String) {
        val stepIngredientCollection =
            db.collection(recipesPath).document(recipeId).collection(stepsPath).document(stepId)
                .collection(ingredientsPath)
        val ingredientResult = stepIngredientCollection.get().await()
        for (ingredientDocument in ingredientResult) {
            stepIngredientCollection.document(ingredientDocument.id)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }
    }

    override suspend fun deletePizStepWithIngredients(id: String, recipeId: String) {
        // delete steps with their ingredients
        val stepDocumentRef = db.collection(recipesPath).document(recipeId).collection(stepsPath).document(id)
        val stepDocument = stepDocumentRef.get().await()
        // delete step ingredients
        deletePizStepIngredientsForStepId(stepDocument.id, recipeId)
        // delete step
        stepDocumentRef
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    override suspend fun deletePizStepIngredient(id: String, recipeId: String, stepId: String) {
        db.collection(recipesPath).document(recipeId).collection(stepsPath).document(stepId)
            .collection(ingredientsPath).document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    override suspend fun deletePizIngredient(id: String, recipeId: String) {
        db.collection(recipesPath).document(recipeId).collection(ingredientsPath).document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    override suspend fun insertPizRecipe(pizRecipe: PizRecipe) {
        val recipeRef = db.collection(recipesPath).document(pizRecipe.id)
        // base recipe
        recipeRef
            .set(pizRecipe.toHashMap())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        // don't need to add empty collections
    }

    override suspend fun insertPizIngredients(pizIngredients: List<PizIngredient>, recipeId: String) {
        pizIngredients.forEach {
            insertPizIngredient(it, recipeId)
        }
    }

    override suspend fun insertPizIngredient(pizIngredient: PizIngredient, recipeId: String) {
        val ingredientsRef = db.collection(recipesPath).document(recipeId).collection(ingredientsPath)
        ingredientsRef.document(pizIngredient.id)
            .set(pizIngredient.toHashMap())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    override suspend fun insertPizStepIngredient(pizStepIngredient: PizIngredient, recipeId: String, stepId: String) {
        val stepIngredientsRef = db.collection(recipesPath).document(recipeId).collection(stepsPath).document(stepId)
            .collection(ingredientsPath)
        stepIngredientsRef.document(pizStepIngredient.id)
            .set(pizStepIngredient.toHashMap())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    override suspend fun insertPizSteps(pizSteps: List<PizStepWithIngredients>, recipeId: String) {
        pizSteps.forEach {
            insertPizStep(it, recipeId)
        }
    }

    override suspend fun insertPizStep(pizStep: PizStepWithIngredients, recipeId: String) {
        val stepsRef = db.collection(recipesPath).document(recipeId).collection(stepsPath)
        stepsRef.document(pizStep.id)
            .set(pizStep.toHashMap())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    override suspend fun insertPizStepIngredients(
        pizStepIngredients: List<PizIngredient>,
        recipeId: String,
        stepId: String
    ) {
        val stepIngredientsRef = db.collection(recipesPath).document(recipeId).collection(stepsPath).document(stepId)
            .collection(ingredientsPath)
        pizStepIngredients.forEach {
            stepIngredientsRef.document(it.id)
                .set(it.toHashMap())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    override suspend fun saveRecipeImage(imageName: String, bitmap: ImageBitmap) {

        val storageRef = storage.reference

        val recipeImgRef = storageRef.child("$imagePath/$imageName.png")

        val androidBitmap = bitmap.asAndroidBitmap()
        androidBitmap.setHasAlpha(true) // important for saving transparent background
        val baos = ByteArrayOutputStream()
        androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

//        var metadata = storageMetadata {
//            contentType = "image/png"
//        }

        val uploadTask = recipeImgRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            // TODO
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            // TODO
        }
    }

    override suspend fun updatePizRecipe(pizRecipe: PizRecipe) {
        db.collection(recipesPath).document(pizRecipe.id)
            .set(pizRecipe.toHashMap())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    //helper
    private fun firestoreWrongFormatException(e: java.lang.Exception) {
        Log.e(TAG, "Firestore document has wrong format:")
        e.printStackTrace()
    }

}