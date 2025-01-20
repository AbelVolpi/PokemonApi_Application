package com.abelvolpi.pokemonapi.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenericPokemonResponse(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
) : Parcelable
