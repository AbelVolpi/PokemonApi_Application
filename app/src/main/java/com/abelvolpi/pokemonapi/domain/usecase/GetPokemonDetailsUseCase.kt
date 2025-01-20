package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(pokemonName: String): DetailedPokemon {
        return repository.getPokemonInfo(pokemonName)
    }
}
