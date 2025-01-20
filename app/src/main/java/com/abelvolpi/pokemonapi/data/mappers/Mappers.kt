package com.abelvolpi.pokemonapi.data.mappers

import com.abelvolpi.pokemonapi.data.models.DetailedPokemonResponse
import com.abelvolpi.pokemonapi.data.models.GenericPokemonResponse
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import com.abelvolpi.pokemonapi.data.models.StatItemResponse
import com.abelvolpi.pokemonapi.data.models.StatResponse
import com.abelvolpi.pokemonapi.data.models.SubTypeResponse
import com.abelvolpi.pokemonapi.data.models.TypeResponse
import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.models.GenericPokemon
import com.abelvolpi.pokemonapi.domain.models.PokemonList
import com.abelvolpi.pokemonapi.domain.models.Stat
import com.abelvolpi.pokemonapi.domain.models.StatItem
import com.abelvolpi.pokemonapi.domain.models.SubType
import com.abelvolpi.pokemonapi.domain.models.Type

fun PokemonListResponse.toDomain(): PokemonList {
    return PokemonList(
        results = results.map { it.toDomain() },
        nextPageUrl = nextPageUrl
    )
}

fun GenericPokemonResponse.toDomain(): GenericPokemon {
    val number = url.split("/")[url.split("/").size - 2]
    return GenericPokemon(
        name = name,
        url = url,
        number = number
    )
}

fun DetailedPokemonResponse.toDomain(): DetailedPokemon {
    return DetailedPokemon(
        name = name,
        types = typeResponses.map { it.toDomain() },
        weight = weight,
        height = height,
        stats = stats.map { it.toDomain() }
    )
}

fun TypeResponse.toDomain(): Type {
    return Type(
        type = type.toDomain()
    )
}

fun SubTypeResponse.toDomain(): SubType {
    return SubType(
        typeName = typeName
    )
}

fun StatItemResponse.toDomain(): StatItem {
    return StatItem(
        baseStat = baseStat,
        stat = statResponse.toDomain()
    )
}

fun StatResponse.toDomain(): Stat {
    return Stat(
        name = name
    )
}
