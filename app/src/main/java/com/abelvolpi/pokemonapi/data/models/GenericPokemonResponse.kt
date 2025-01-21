package com.abelvolpi.pokemonapi.data.models

import com.squareup.moshi.Json

data class GenericPokemonResponse(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)
