package com.abelvolpi.pokemonapi.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailedPokemonResponse(
    @Json(name = "name") val name: String,
    @Json(name = "types") val typeResponses: List<TypeResponse>,
    @Json(name = "weight") val weight: Float,
    @Json(name = "height") val height: Float,
    @Json(name = "stats") val stats: List<StatItemResponse>
) : Parcelable

@Parcelize
data class TypeResponse(
    @Json(name = "type") val type: SubTypeResponse
) : Parcelable

@Parcelize
data class SubTypeResponse(
    @Json(name = "name") val typeName: String = ""
) : Parcelable

@Parcelize
data class StatItemResponse(
    @Json(name = "base_stat") val baseStat: Int,
    @Json(name = "stat") val statResponse: StatResponse
) : Parcelable

@Parcelize
data class StatResponse(
    @Json(name = "name") val name: String
) : Parcelable
