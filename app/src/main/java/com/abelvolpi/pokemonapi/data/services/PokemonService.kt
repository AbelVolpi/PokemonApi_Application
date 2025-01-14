package com.abelvolpi.pokemonapi.data.services

import com.abelvolpi.pokemonapi.data.models.DetailedPokemon
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?
    ): PokemonListResponse

    @GET("pokemon/{pokemon_name}")
    suspend fun getPokemonInfo(
        @Path("pokemon_name") pokemonName: String
    ): DetailedPokemon
}
