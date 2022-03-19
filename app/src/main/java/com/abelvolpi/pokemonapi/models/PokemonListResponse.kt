package com.abelvolpi.pokemonapi.models

import com.squareup.moshi.Json

data class PokemonListResponse(
    @Json(name = "results") val results: List<GenericPokemon>,
    @Json(name = "next") val nextPageUrl: String?,
)