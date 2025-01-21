package com.abelvolpi.pokemonapi.data.repository

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.DetailedPokemonResponse
import com.abelvolpi.pokemonapi.data.models.GenericPokemonResponse
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import com.abelvolpi.pokemonapi.data.models.StatItemResponse
import com.abelvolpi.pokemonapi.data.models.StatResponse
import com.abelvolpi.pokemonapi.data.models.SubTypeResponse
import com.abelvolpi.pokemonapi.data.models.TypeResponse
import com.abelvolpi.pokemonapi.data.services.PokemonService
import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.models.GenericPokemon
import com.abelvolpi.pokemonapi.domain.models.PokemonList
import com.abelvolpi.pokemonapi.domain.models.Stat
import com.abelvolpi.pokemonapi.domain.models.StatItem
import com.abelvolpi.pokemonapi.domain.models.SubType
import com.abelvolpi.pokemonapi.domain.models.Type
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import com.abelvolpi.pokemonapi.utils.Constants.URLS.PNG
import com.abelvolpi.pokemonapi.utils.Constants.URLS.POKEMON_IMAGE_URL
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonRepositoryImplTest {

    private lateinit var pokemonService: PokemonService
    private lateinit var pokemonRepository: PokemonRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        pokemonService = mockk()
        pokemonRepository = PokemonRepositoryImpl(pokemonService, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `should return success when getPokemonList call PokemonService with correct parameters`() =
        runTest {
            // Given
            val offset = 0
            val limit = 20
            val mockResponse = createMockPokemonListResponse()
            val expectedDomainList = createExpectedPokemonList()
            coEvery { pokemonService.getPokemonList(offset, limit) } returns mockResponse

            // When
            val result = pokemonRepository.getPokemonList(offset, limit)

            // Then
            coVerify(exactly = 1) { pokemonService.getPokemonList(offset, limit) }
            assert(result == expectedDomainList)
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
            val result = try {
                pokemonRepository.getPokemonList(offset, limit)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(result == exception)
        }

    @Test
    fun `should return success when getPokemonInfo call PokemonService with correct parameters`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val mockResponse = createMockDetailedPokemonResponse(pokemonName)
            val expectedDomainDetailedPokemon = createExpectedDetailedPokemon(pokemonName)
            coEvery { pokemonService.getPokemonInfo(pokemonName) } returns mockResponse

            // When
            val result = pokemonRepository.getPokemonInfo(pokemonName)

            // Then
            coVerify(exactly = 1) { pokemonService.getPokemonInfo(pokemonName) }
            assert(result == expectedDomainDetailedPokemon)
        }

    @Test
    fun `should return error when getPokemonInfo throws exception`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val exception = Throwable("Network error")
            coEvery { pokemonService.getPokemonInfo(pokemonName) } throws exception

            // When
            val result = try {
                pokemonRepository.getPokemonInfo(pokemonName)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(result == exception)
        }

    private fun createMockPokemonListResponse() = PokemonListResponse(
        results = listOf(
            GenericPokemonResponse(
                name = "pikachu",
                url = "https://pokeapi.co/api/v2/pokemon/25/"
            ),
            GenericPokemonResponse(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/1/"
            )
        ),
        nextPageUrl = "next_url"
    )

    private fun createExpectedPokemonList() = PokemonList(
        listOf(
            GenericPokemon(
                name = "pikachu",
                url = "https://pokeapi.co/api/v2/pokemon/25/",
                number = "25",
                imageUrl = POKEMON_IMAGE_URL + "25" + PNG
            ),
            GenericPokemon(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/1/",
                number = "1",
                imageUrl = POKEMON_IMAGE_URL + "1" + PNG
            )
        ),
        nextPageUrl = "next_url"
    )

    private fun createMockDetailedPokemonResponse(pokemonName: String) = DetailedPokemonResponse(
        name = pokemonName,
        typeResponses = listOf(TypeResponse(SubTypeResponse(typeName = "electric"))),
        weight = 6.0f,
        height = 0.4f,
        stats = listOf(
            StatItemResponse(baseStat = 35, statResponse = StatResponse(name = "hp")),
            StatItemResponse(baseStat = 55, statResponse = StatResponse(name = "attack"))
        )
    )

    private fun createExpectedDetailedPokemon(pokemonName: String) = DetailedPokemon(
        name = pokemonName,
        types = listOf(Type(SubType(typeName = "electric"))),
        weight = 6.0f,
        height = 0.4f,
        stats = listOf(
            StatItem(baseStat = 35, stat = Stat(name = "hp")),
            StatItem(baseStat = 55, stat = Stat(name = "attack"))
        )
    )
}
