package com.abelvolpi.pokemonapi.presentation.mappers

import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.models.GenericPokemon
import com.abelvolpi.pokemonapi.domain.models.PokemonList
import com.abelvolpi.pokemonapi.domain.models.StatItem
import com.abelvolpi.pokemonapi.presentation.models.DetailedPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.models.GenericPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.models.PokemonListUiModel
import com.abelvolpi.pokemonapi.presentation.models.StatUiModel

fun PokemonList.toUiModel(): PokemonListUiModel {
    return PokemonListUiModel(
        results = results.map { it.toUiModel() }
    )
}

fun DetailedPokemon.toUiModel(): DetailedPokemonUiModel {
    return DetailedPokemonUiModel(
        name = name,
        types = types.map { it.type.typeName },
        weight = weight,
        height = height,
        stats = stats.map { it.toUiModel() }
    )
}

fun StatItem.toUiModel(): StatUiModel {
    return StatUiModel(
        value = baseStat,
        name = stat.name
    )
}

fun GenericPokemon.toUiModel(): GenericPokemonUiModel {
    return GenericPokemonUiModel(
        name = name,
        number = number,
        imageUrl = imageUrl
    )
}
