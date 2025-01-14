package com.abelvolpi.pokemonapi.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailedPokemon(
    @Json(name = "name") val name: String,
    @Json(name = "types") val types: List<Type>,
    @Json(name = "weight") val weight: Float,
    @Json(name = "height") val height: Float,
    @Json(name = "stats") val stats: List<StatItem>
) : Parcelable

@Parcelize
data class StatItem(
    @Json(name = "base_stat") val baseStat: Int,
    @Json(name = "stat") val stat: Stat
) : Parcelable

@Parcelize
data class Stat(
    @Json(name = "name") val name: String
) : Parcelable
