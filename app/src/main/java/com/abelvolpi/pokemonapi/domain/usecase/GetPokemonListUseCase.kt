package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(offset: Int?, limit: Int?): PokemonListResponse {
        return repository.getPokemonList(offset, limit).getOrThrow()
    }
}
