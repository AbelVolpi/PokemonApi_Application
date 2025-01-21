package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.domain.models.GenericPokemon
import com.abelvolpi.pokemonapi.domain.models.PokemonList
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPokemonListUseCaseTest {

    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        pokemonRepository = mockk()
        getPokemonListUseCase = GetPokemonListUseCase(pokemonRepository)
    }

    @Test
    fun `should return pokemon list when repository returns data`() =
        runTest {
            // Given
            val offset = 0
            val limit = 20
            val expectedPokemonList = createExpectedPokemonList()
            coEvery { pokemonRepository.getPokemonList(offset, limit) } returns expectedPokemonList

            // When
            val result = getPokemonListUseCase(offset, limit)

            // Then
            coVerify(exactly = 1) { pokemonRepository.getPokemonList(offset, limit) }
            assert(result == expectedPokemonList)
        }

    @Test
    fun `should return error when repository throws exception`() =
        runTest {
            // Given
            val offset = 0
            val limit = 20
            val exception = Throwable("Network error")
            coEvery { pokemonRepository.getPokemonList(offset, limit) } throws exception

            // When
            val response = try {
                getPokemonListUseCase(offset, limit)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(response == exception)
        }

    private fun createExpectedPokemonList() = PokemonList(
        listOf(
            GenericPokemon(
                name = "pikachu",
                url = "https://pokeapi.co/api/v2/pokemon/25/",
                number = "25",
                imageUrl = "image_url"
            ),
            GenericPokemon(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/1/",
                number = "1",
                imageUrl = "image_url"
            )
        ),
        nextPageUrl = "next_url"
    )
}
