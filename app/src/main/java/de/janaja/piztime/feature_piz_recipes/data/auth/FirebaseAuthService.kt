package de.janaja.piztime.feature_piz_recipes.data.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.janaja.piztime.feature_piz_recipes.domain.auth.AuthService
import de.janaja.piztime.feature_piz_recipes.domain.util.Resource
import de.janaja.piztime.feature_piz_recipes.presentation.util.TAG
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

// TODO dependency inject firebase auth
class FirebaseAuthService : AuthService {

    private var auth = Firebase.auth

//    private var user = auth.currentUser

    private var _hasUser = mutableStateOf(false)
    override val hasUser: State<Boolean> = _hasUser


    override suspend fun logIn(email: String, password: String): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            _hasUser.value = true
            Log.d(TAG, "signInWithEmail:success")
            emit(Resource.Success(Unit))
        }.catch {
            Log.w(TAG, "signInWithEmail:failure", it)
            emit(Resource.Error(it.message.toString()))
        }
    }

    override suspend fun logOut(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            auth.signOut()
            _hasUser.value = false
            emit(Resource.Success(Unit))
        }.catch {
            Log.w(TAG, "signInWithEmail:failure", it)
            emit(Resource.Error(it.message.toString()))
        }

    }
}