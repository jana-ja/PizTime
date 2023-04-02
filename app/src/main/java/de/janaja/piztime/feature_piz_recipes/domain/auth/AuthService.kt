package de.janaja.piztime.feature_piz_recipes.domain.auth

import androidx.compose.runtime.State
import de.janaja.piztime.feature_piz_recipes.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthService {

    val hasUser: State<Boolean>
    suspend fun logIn(email: String, password: String): Flow<Resource<Boolean>>

    suspend fun logOut(): Flow<Resource<Boolean>>
}