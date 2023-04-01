package de.janaja.piztime.feature_piz_recipes.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipeWithDetails
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients


// domain -> data

fun PizRecipe.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "title" to title,
        "feature" to feature,
        "imageName" to imageName,
        "prepTime" to prepTime,
        "id" to id
    )
}

fun PizRecipeWithDetails.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "title" to title,
        "feature" to feature,
        "imageName" to imageName,
        "prepTime" to prepTime,
        "id" to id
    )
}

fun PizIngredient.toHashMap(): Map<String, Any> {
    return hashMapOf(
        "ingredient" to ingredient,
        "baseAmount" to baseAmount,
        "id" to id
    )
}

fun PizStepWithIngredients.toHashMap(): Map<String, Any> {
    return hashMapOf(
        "description" to description,
        "ingredients" to ingredients,
        "id" to id
    )
}


// data -> domain
@Throws(java.lang.NullPointerException::class)
fun DocumentSnapshot.toPizRecipe(): PizRecipe {
    return PizRecipe(
        title = data!!["title"].toString(),
        feature = data!!["feature"].toString(),
        imageName = data!!["imageName"].toString(),
        prepTime = data!!["prepTime"].toString().toDouble(),
        id = id
    )
}

@Throws(java.lang.NullPointerException::class)
fun DocumentSnapshot.toPizIngredient(): PizIngredient {
    return PizIngredient(
        ingredient = data!!["ingredient"].toString(),
        baseAmount = data!!["baseAmount"].toString().toDouble(),
        id = id
    )
}

@Throws(java.lang.NullPointerException::class)
fun DocumentSnapshot.toPizStepWithIngredients(ingredients: List<PizIngredient>): PizStepWithIngredients {
    return PizStepWithIngredients(
        description = data!!["description"].toString(),
        ingredients = ingredients,
        id = id
    )
}

@Throws(java.lang.NullPointerException::class)
fun DocumentSnapshot.toPizStepWithoutIngredients(): PizStepWithIngredients {
    return PizStepWithIngredients(
        description = data!!["description"].toString(),
        ingredients = listOf(),
        id = id
    )
}

@Throws(java.lang.NullPointerException::class)
fun DocumentSnapshot.toPizRecipeWithDetails(
    ingredients: List<PizIngredient>,
    steps: List<PizStepWithIngredients>
): PizRecipeWithDetails {
    return PizRecipeWithDetails(
        title = data!!["title"].toString(),
        feature = data!!["feature"].toString(),
        imageName = data!!["imageName"].toString(),
        prepTime = data!!["prepTime"].toString().toDouble(),
        id = id,
        ingredients = ingredients,
        steps = steps,
    )
}











