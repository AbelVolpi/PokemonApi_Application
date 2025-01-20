package com.abelvolpi.pokemonapi.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonListUiModel(
    val results: List<GenericPokemonUiModel>
) : Parcelable
