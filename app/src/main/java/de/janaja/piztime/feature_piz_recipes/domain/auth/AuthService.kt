package de.janaja.piztime.feature_piz_recipes.domain.auth

import de.janaja.piztime.feature_piz_recipes.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthService {

    suspend fun logIn(email: String, password: String): Flow<Resource<Boolean>>

    suspend fun logOut(): Flow<Resource<Boolean>>
}