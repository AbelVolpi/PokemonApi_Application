package com.abelvolpi.pokemonapi.di

import com.abelvolpi.pokemonapi.data.repository.PokemonRepositoryImpl
import com.abelvolpi.pokemonapi.data.services.PokemonService
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providePokemonService(retrofit: Retrofit): PokemonService = retrofit.create(PokemonService::class.java)

    @Provides
    fun providePokemonRepository(
        pokemonService: PokemonService
    ): PokemonRepository = PokemonRepositoryImpl(pokemonService)

    @Provides
    fun provideGetPokemonListUseCase(
        repository: PokemonRepository
    ): GetPokemonListUseCase = GetPokemonListUseCase(repository)
}
