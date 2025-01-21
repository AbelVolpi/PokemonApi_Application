package com.abelvolpi.pokemonapi.domain.models

data class PokemonList(
    val results: List<GenericPokemon>,
    val nextPageUrl: String?
)
