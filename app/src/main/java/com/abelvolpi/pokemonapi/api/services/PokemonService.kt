package com.abelvolpi.pokemonapi.api.services

import com.abelvolpi.pokemonapi.models.DetailedPokemon
import com.abelvolpi.pokemonapi.models.PokemonListResponse
import com.abelvolpi.pokemonapi.utils.Constants.URLS.POKEMON
import com.abelvolpi.pokemonapi.utils.Constants.URLS.POKEMON_INFO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET(POKEMON)
    suspend fun getPokemonList(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?
    ): PokemonListResponse

    @GET(POKEMON_INFO)
    suspend fun getPokemonInfo(
        @Path("pokemon_name") pokemonName: String
    ): DetailedPokemon

}