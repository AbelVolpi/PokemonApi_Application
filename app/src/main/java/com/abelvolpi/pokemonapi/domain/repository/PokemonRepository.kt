package com.abelvolpi.pokemonapi.domain.repository

import com.abelvolpi.pokemonapi.data.models.DetailedPokemon
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int?, limit: Int?): Result<PokemonListResponse>
    suspend fun getPokemonInfo(pokemonName: String): Result<DetailedPokemon>
}