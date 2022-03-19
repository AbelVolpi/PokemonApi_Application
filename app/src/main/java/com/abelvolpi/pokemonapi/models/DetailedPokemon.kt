package com.abelvolpi.pokemonapi.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Parcelize
data class DetailedPokemon(
    @Json(name = "name") val name: String,
    @Json(name = "types") val types: List<Type>,
    @Json(name = "weight") val weight: Float,
    @Json(name = "height") val height: Float,
//    @Json(name = "stats") val stats: ArrayList<Stat>,

//    val hp: String,
//    val atk: String,
//    val def: String,
//    val spd: String,
//    val exp: String
) : Parcelable
