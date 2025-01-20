package com.abelvolpi.pokemonapi.data.repository

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.DetailedPokemonResponse
import com.abelvolpi.pokemonapi.data.models.GenericPokemonResponse
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import com.abelvolpi.pokemonapi.data.models.StatResponse
import com.abelvolpi.pokemonapi.data.models.StatItemResponse
import com.abelvolpi.pokemonapi.data.models.SubTypeResponse
import com.abelvolpi.pokemonapi.data.models.TypeResponse
import com.abelvolpi.pokemonapi.data.services.PokemonService
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonRepositoryImplTest {

    private lateinit var pokemonService: PokemonService
    private lateinit var repository: PokemonRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        pokemonService = mockk()
        repository = PokemonRepositoryImpl(pokemonService, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `should return success when getPokemonList call PokemonService with correct parameters`() =
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
            coEvery { pokemonService.getPokemonList(offset, limit) } returns mockResponse

            // When
            val response = repository.getPokemonList(offset, limit)

            // Then
            coVerify(exactly = 1) { pokemonService.getPokemonList(offset, limit) }
            assert(response == mockResponse)
        }

    @Test
    fun `should return error when getPokemonList throws an internet exception`() =
        runTest {
            // Given
            val offset = 0
            val limit = 20
            val exception = Throwable("Network error")
            coEvery { pokemonService.getPokemonList(offset, limit) } throws exception

            // When
            val response = try {
                repository.getPokemonList(offset, limit)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(response == exception)
        }

    @Test
    fun `should return success when getPokemonInfo call PokemonService with correct parameters`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val mockResponse = DetailedPokemonResponse(
                name = "pikachu",
                typeResponses = listOf(TypeResponse(SubTypeResponse(typeName = "electric"))),
                weight = 6.0f,
                height = 0.4f,
                stats = listOf(
                    StatItemResponse(baseStat = 35, statResponse = StatResponse(name = "hp")),
                    StatItemResponse(baseStat = 55, statResponse = StatResponse(name = "attack"))
                )
            )
            coEvery { pokemonService.getPokemonInfo(pokemonName) } returns mockResponse

            // When
            val response = repository.getPokemonInfo(pokemonName)

            // Then
            coVerify(exactly = 1) { pokemonService.getPokemonInfo(pokemonName) }
            assert(response == mockResponse)
        }

    @Test
    fun `should return error when getPokemonInfo throws exception`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val exception = Throwable("Network error")
            coEvery { pokemonService.getPokemonInfo(pokemonName) } throws exception

            // When
            val response = try {
                repository.getPokemonInfo(pokemonName)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(response == exception)
        }
}
