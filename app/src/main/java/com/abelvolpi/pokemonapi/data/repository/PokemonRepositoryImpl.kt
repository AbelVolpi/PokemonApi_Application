package com.abelvolpi.pokemonapi.data.repository

import com.abelvolpi.pokemonapi.data.api.services.PokemonService
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import com.abelvolpi.pokemonapi.data.models.DetailedPokemon
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepositoryImpl(
    private val pokemonService: PokemonService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonRepository {
    override suspend fun getPokemonList(offset: Int?, limit: Int?): Result<PokemonListResponse> {
        return withContext(ioDispatcher) {
            try {
                val response = pokemonService.getPokemonList(offset, limit)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getPokemonInfo(pokemonName: String): Result<DetailedPokemon> {
        return withContext(ioDispatcher) {
            try {
                val response = pokemonService.getPokemonInfo(pokemonName)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}