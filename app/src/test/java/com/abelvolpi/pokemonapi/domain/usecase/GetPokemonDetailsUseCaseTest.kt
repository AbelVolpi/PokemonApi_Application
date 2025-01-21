package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.DetailedPokemonResponse
import com.abelvolpi.pokemonapi.data.models.StatItemResponse
import com.abelvolpi.pokemonapi.data.models.StatResponse
import com.abelvolpi.pokemonapi.data.models.SubTypeResponse
import com.abelvolpi.pokemonapi.data.models.TypeResponse
import com.abelvolpi.pokemonapi.data.repository.PokemonRepositoryImpl
import com.abelvolpi.pokemonapi.data.services.PokemonService
import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.models.Stat
import com.abelvolpi.pokemonapi.domain.models.StatItem
import com.abelvolpi.pokemonapi.domain.models.SubType
import com.abelvolpi.pokemonapi.domain.models.Type
import com.abelvolpi.pokemonapi.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPokemonDetailsUseCaseTest {

    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var getPokemonDetailsUseCase: GetPokemonDetailsUseCase
    private lateinit var pokemonService: PokemonService

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        pokemonService = mockk()
        pokemonRepository = PokemonRepositoryImpl(pokemonService, mainDispatcherRule.testDispatcher)
        getPokemonDetailsUseCase = GetPokemonDetailsUseCase(pokemonRepository)
    }

    @Test
    fun `should return pokemon details when repository returns data`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val mockResponse = createMockDetailedPokemonResponse(pokemonName)
            val expectedDetailedPokemon = createDetailedPokemonDomainResponse(pokemonName)
            coEvery { pokemonService.getPokemonInfo(pokemonName) } returns mockResponse

            // When
            val result = getPokemonDetailsUseCase(pokemonName)

            // Then
            coVerify(exactly = 1) { pokemonService.getPokemonInfo(pokemonName) }
            assert(result == expectedDetailedPokemon)
        }

    @Test
    fun `should return error when repository throws exception`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val exception = Throwable("Network error")
            coEvery { pokemonService.getPokemonInfo(pokemonName) } throws exception

            // When
            val result = try {
                getPokemonDetailsUseCase(pokemonName)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(result == exception)
        }

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

    private fun createDetailedPokemonDomainResponse(pokemonName: String) = DetailedPokemon(
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
