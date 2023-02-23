package de.janaja.piztime.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.janaja.piztime.feature_piz_recipes.data.repository.RepositoryImpl
import de.janaja.piztime.feature_piz_recipes.domain.repository.Repository
import de.janaja.piztime.feature_piz_recipes.domain.use_case.*
import de.janaja.piztime.feature_piz_recipes.data.local.PizRecipeDatabase
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
    fun providePizRecipeRepository(db: PizRecipeDatabase): Repository {
        return RepositoryImpl(db.pizRecipeDao, db.pizIngredientDao, db.pizStepDao, db.pizStepIngredientDao)
    }

    @Provides
    @Singleton
    fun providePizrecipeUseCase(repository: Repository): AllPizRecipeUseCases{
        return AllPizRecipeUseCases(
            initDbIfEmptyUseCase = InitDbIfEmptyUseCase(repository),
            getAllPizRecipesUseCase = GetAllPizRecipesUseCase(repository),
            getPizRecipeUseCase = GetPizRecipeUseCase(repository),
            getPizRecipeWithDetailsUseCase = GetPizRecipeWithDetailsUseCase(repository),
            updatePizRecipeUseCase = UpdatePizRecipeUseCase(repository),
            getIngredientsUseCase = GetIngredientsUseCase(repository),
            updateIngredientsUseCase = UpdateIngredientsUseCase(repository),
            getStepsWithIngredientsUseCase = GetStepsWithIngredientsUseCase(repository),
            updateStepsWithIngredientsUseCase = UpdateStepsWithIngredientsUseCase(repository)
        )
    }
}