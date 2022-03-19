package com.abelvolpi.pokemonapi.api.repository

import com.abelvolpi.pokemonapi.api.retrofit.ApiProvider
import com.abelvolpi.pokemonapi.api.services.PokemonService

object PokemonRepository {

    private val pokemonService = ApiProvider.retrofit.create(PokemonService::class.java)

    suspend fun getPokemonList(offset: Int?, limit: Int?) = pokemonService.getPokemonList(offset, limit)

    suspend fun getPokemonInfo(pokemonName: String) = pokemonService.getPokemonInfo(pokemonName)

}