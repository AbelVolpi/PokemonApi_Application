package com.abelvolpi.pokemonapi.domain.models

data class DetailedPokemon(
    val name: String,
    val types: List<Type>,
    val weight: Float,
    val height: Float,
    val stats: List<StatItem>
)

data class Type(
    val type: SubType
)

data class SubType(
    val typeName: String = ""
)

data class StatItem(
    val baseStat: Int,
    val stat: Stat
)

data class Stat(
    val name: String
)
