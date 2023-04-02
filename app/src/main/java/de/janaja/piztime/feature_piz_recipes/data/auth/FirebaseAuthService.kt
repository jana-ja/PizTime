package de.janaja.piztime.feature_piz_recipes.data.auth

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.janaja.piztime.feature_piz_recipes.domain.auth.AuthService
import de.janaja.piztime.feature_piz_recipes.domain.util.Resource
import de.janaja.piztime.feature_piz_recipes.presentation.util.TAG
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

// TODO dependency inject firebase auth
class FirebaseAuthService: AuthService {

    private var auth = Firebase.auth

//    private var user = auth.currentUser

    override suspend fun logIn(email: String, password: String): Flow<Resource<Boolean>> {
       return flow{
           emit(Resource.Loading())
           val authResult = auth.signInWithEmailAndPassword(email, password).await()
           Log.d(TAG, "signInWithEmail:success")
           emit(Resource.Success(true))
       }.catch {
           Log.w(TAG, "signInWithEmail:failure", it)
           emit(Resource.Error(it.message.toString()))
       }
    }

    override suspend fun logOut() : Flow<Resource<Boolean>>{
        return flow{
            emit(Resource.Loading())
            auth.signOut()
            emit(Resource.Success(false))
        }.catch {
            Log.w(TAG, "signInWithEmail:failure", it)
            emit(Resource.Error(it.message.toString()))
        }

    }
}