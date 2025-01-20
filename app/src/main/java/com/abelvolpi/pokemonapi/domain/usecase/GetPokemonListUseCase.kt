package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.domain.models.PokemonList
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(offset: Int?, limit: Int?): PokemonList {
        return repository.getPokemonList(offset, limit)
    }
}
