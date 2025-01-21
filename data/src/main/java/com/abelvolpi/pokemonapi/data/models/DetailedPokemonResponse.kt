package com.abelvolpi.pokemonapi.data.models

import com.squareup.moshi.Json

data class DetailedPokemonResponse(
    @Json(name = "name") val name: String,
    @Json(name = "types") val typeResponses: List<TypeResponse>,
    @Json(name = "weight") val weight: Float,
    @Json(name = "height") val height: Float,
    @Json(name = "stats") val stats: List<StatItemResponse>
)

data class TypeResponse(
    @Json(name = "type") val type: SubTypeResponse
)

data class SubTypeResponse(
    @Json(name = "name") val typeName: String = ""
)

data class StatItemResponse(
    @Json(name = "base_stat") val baseStat: Int,
    @Json(name = "stat") val statResponse: StatResponse
)

data class StatResponse(
    @Json(name = "name") val name: String
)
