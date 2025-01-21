package com.abelvolpi.pokemonapi.data.repository

import com.abelvolpi.pokemonapi.data.mappers.toDomain
import com.abelvolpi.pokemonapi.data.services.PokemonService
import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.models.PokemonList
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepositoryImpl(
    private val pokemonService: PokemonService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonRepository {
    override suspend fun getPokemonList(offset: Int?, limit: Int?): PokemonList {
        return withContext(dispatcher) {
            val pokemonListResponse = pokemonService.getPokemonList(offset, limit)
            pokemonListResponse.toDomain()
        }
    }

    override suspend fun getPokemonInfo(pokemonName: String): DetailedPokemon {
        return withContext(dispatcher) {
            val detailedPokemonResponse = pokemonService.getPokemonInfo(pokemonName)
            detailedPokemonResponse.toDomain()
        }
    }
}
