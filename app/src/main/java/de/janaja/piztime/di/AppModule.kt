package de.janaja.piztime.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.janaja.piztime.feature_piz_recipes.data.auth.FirebaseAuthService
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import de.janaja.piztime.feature_piz_recipes.domain.use_case.*
import de.janaja.piztime.feature_piz_recipes.data.local.PizRecipeDatabase
import de.janaja.piztime.feature_piz_recipes.data.repository.RepositoryFirestore
import de.janaja.piztime.feature_piz_recipes.domain.auth.AuthService
import javax.inject.Singleton

// wenn man mehrere features hat könnte man auch pro feature ein di ding machen
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePizRecipeDatabase(app: Application): PizRecipeDatabase {
        return Room.databaseBuilder(
            app,
            PizRecipeDatabase::class.java,
            PizRecipeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePizRecipeRepository(/*db: PizRecipeDatabase, app: Application*/): Repository {
        return RepositoryFirestore()//RepositoryRoom(db, app)
    }

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return FirebaseAuthService()
    }

    @Provides
    @Singleton
    fun providePizrecipeUseCase(repository: Repository): AllPizRecipeUseCases {
        return AllPizRecipeUseCases(
            initDbIfEmptyUseCase = InitDbIfEmptyUseCase(repository),
            addPizRecipeUseCase = AddPizRecipeUseCase(repository),
            loadAllPizRecipesUseCase = LoadAllPizRecipesUseCase(repository),
            loadPizRecipeWithDetailsUseCase = LoadPizRecipeWithDetailsUseCase(repository),
            getAllPizRecipesFlowUseCase = GetAllPizRecipesFlowUseCase(repository),
            getPizRecipeWithDetailsFlowUseCase = GetPizRecipeWithDetailsFlowUseCase(repository),
            getPizRecipeUseCase = GetPizRecipeUseCase(repository) ,
            getRecipeImageUseCase = GetRecipeImageUseCase(repository),
            getIngredientUseCase = GetIngredientUseCase(repository),
            getStepWithoutIngredientsUseCase = GetStepWithoutIngredientsUseCase(repository),
            deleteIngredientUseCase = DeleteIngredientUseCase(repository),
            deleteStepUseCase = DeleteStepUseCase(repository),
            updateIngredientUseCase = UpdateIngredientUseCase(repository),
            updateStepUseCase = UpdateStepUseCase(repository),
            updateRecipeUseCase = UpdateRecipeUseCase(repository),
            saveRecipeImageUseCase = SaveRecipeImageUseCase(repository),
            resetPizRecipeWithDetailsFlowUseCase = ResetPizRecipeWithDetailsFlowUseCase(repository)

        )
    }
}