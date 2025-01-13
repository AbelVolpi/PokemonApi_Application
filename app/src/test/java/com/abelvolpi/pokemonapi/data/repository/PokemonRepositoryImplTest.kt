package com.abelvolpi.pokemonapi.data.repository

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.DetailedPokemon
import com.abelvolpi.pokemonapi.data.models.GenericPokemon
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import com.abelvolpi.pokemonapi.data.models.Stat
import com.abelvolpi.pokemonapi.data.models.StatItem
import com.abelvolpi.pokemonapi.data.models.SubType
import com.abelvolpi.pokemonapi.data.models.Type
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
                    GenericPokemon(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/"),
                    GenericPokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
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
            val mockResponse = DetailedPokemon(
                name = "pikachu",
                types = listOf(Type(SubType(typeName = "electric"))),
                weight = 6.0f,
                height = 0.4f,
                stats = listOf(
                    StatItem(baseStat = 35, stat = Stat(name = "hp")),
                    StatItem(baseStat = 55, stat = Stat(name = "attack"))
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
