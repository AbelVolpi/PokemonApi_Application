package com.abelvolpi.pokemonapi.data.di

import com.abelvolpi.pokemonapi.data.repository.PokemonRepositoryImpl
import com.abelvolpi.pokemonapi.data.services.PokemonService
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun providePokemonService(retrofit: Retrofit): PokemonService =
        retrofit.create(PokemonService::class.java)

    @Provides
    fun providePokemonRepository(
        pokemonService: PokemonService
    ): PokemonRepository =
        PokemonRepositoryImpl(pokemonService)
}
