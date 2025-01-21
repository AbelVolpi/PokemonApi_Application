package com.abelvolpi.pokemonapi.data.models

import com.squareup.moshi.Json

data class PokemonListResponse(
    @Json(name = "results") val results: List<GenericPokemonResponse>,
    @Json(name = "next") val nextPageUrl: String?
)
