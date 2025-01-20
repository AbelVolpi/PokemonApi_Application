package com.abelvolpi.pokemonapi.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailedPokemonUiModel(
    val name: String,
    val types: List<String>,
    val weight: Float,
    val height: Float,
    val stats: List<StatUiModel>
) : Parcelable

@Parcelize
data class StatUiModel(
    val value: Int,
    val name: String
) : Parcelable
