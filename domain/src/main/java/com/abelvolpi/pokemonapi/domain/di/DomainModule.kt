package com.abelvolpi.pokemonapi.domain.di

import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonDetailsUseCase
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideGetPokemonListUseCase(
        repository: PokemonRepository
    ): GetPokemonListUseCase =
        GetPokemonListUseCase(repository)

    @Provides
    fun provideGetPokemonDetailsUseCase(
        repository: PokemonRepository
    ): GetPokemonDetailsUseCase =
        GetPokemonDetailsUseCase(repository)
}
