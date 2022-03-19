package com.abelvolpi.pokemonapi.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenericPokemon(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String,
    val number: String = url.split("/")[url.split("/").size - 2]
) : Parcelable
