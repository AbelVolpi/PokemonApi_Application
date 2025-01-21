package com.abelvolpi.pokemonapi.domain.repository

import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.models.PokemonList

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int?, limit: Int?): PokemonList
    suspend fun getPokemonInfo(pokemonName: String): DetailedPokemon
}
