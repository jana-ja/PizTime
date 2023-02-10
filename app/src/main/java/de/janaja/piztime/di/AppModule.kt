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
import de.janaja.piztime.model.PizRecipeDatabase
import javax.inject.Singleton

// wenn man mehrere features hat könnte man auch pro feature ein di ding machen
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePizRecipeDatabase(app: Application): PizRecipeDatabase{
        return Room.databaseBuilder(
            app,
            PizRecipeDatabase::class.java,
            PizRecipeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePizRecipeRepository(db: PizRecipeDatabase): Repository {
        return RepositoryImpl(db.pizRecipeDao)
    }

    @Provides
    @Singleton
    fun providePizrecipeUseCase(repository: Repository): PizRecipeUseCases{
        return PizRecipeUseCases(
            getAllPizRecipesUseCase = GetAllPizRecipesUseCase(repository),
            getPizRecipeUseCase = GetPizRecipeUseCase(repository),
            updatePizRecipeUseCase = UpdatePizRecipeUseCase(repository),
            initDbIfEmptyUseCase = InitDbIfEmptyUseCase(repository)
        )
    }
}