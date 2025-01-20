package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.GenericPokemonResponse
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import com.abelvolpi.pokemonapi.data.repository.PokemonRepositoryImpl
import com.abelvolpi.pokemonapi.data.services.PokemonService
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPokemonListUseCaseTest {

    private lateinit var repository: PokemonRepository
    private lateinit var useCase: GetPokemonListUseCase
    private lateinit var pokemonService: PokemonService

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        pokemonService = mockk()
        repository = PokemonRepositoryImpl(pokemonService, mainDispatcherRule.testDispatcher)
        useCase = GetPokemonListUseCase(repository)
    }

    @Test
    fun `should return pokemon list when repository returns data`() =
        runTest {
            // Given
            val offset = 0
            val limit = 20
            val mockResponse = PokemonListResponse(
                results = listOf(
                    GenericPokemonResponse(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/"),
                    GenericPokemonResponse(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
                ),
                nextPageUrl = "next_url"
            )
            coEvery { repository.getPokemonList(offset, limit) } returns mockResponse

            // When
            val response = useCase(offset, limit)

            // Then
            coVerify(exactly = 1) { repository.getPokemonList(offset, limit) }
            assert(response == mockResponse)
        }

    @Test
    fun `should return error when repository throws exception`() =
        runTest {
            // Given
            val offset = 0
            val limit = 20
            val exception = Throwable("Network error")
            coEvery { repository.getPokemonList(offset, limit) } throws exception

            // When
            val response = try {
                useCase(offset, limit)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(response == exception)
        }
}
