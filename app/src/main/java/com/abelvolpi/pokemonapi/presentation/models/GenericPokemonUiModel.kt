package com.abelvolpi.pokemonapi.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenericPokemonUiModel(
    val name: String,
    val number: String,
    val imageUrl: String
) : Parcelable
