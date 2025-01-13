package com.abelvolpi.pokemonapi.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

// TODO create in domain a dataclass with number
@Parcelize
data class GenericPokemon(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String,
    val number: String = url.split("/")[url.split("/").size - 2]
) : Parcelable
