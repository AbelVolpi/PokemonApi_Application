package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.DetailedPokemon
import com.abelvolpi.pokemonapi.data.models.Stat
import com.abelvolpi.pokemonapi.data.models.StatItem
import com.abelvolpi.pokemonapi.data.models.SubType
import com.abelvolpi.pokemonapi.data.models.Type
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

class GetPokemonDetailsUseCaseTest {

    private lateinit var repository: PokemonRepository
    private lateinit var useCase: GetPokemonDetailsUseCase
    private lateinit var pokemonService: PokemonService

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        pokemonService = mockk()
        repository = PokemonRepositoryImpl(pokemonService, mainDispatcherRule.testDispatcher)
        useCase = GetPokemonDetailsUseCase(repository)
    }

    @Test
    fun `should return pokemon details when repository returns data`() =
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
            val response = useCase(pokemonName)

            // Then
            coVerify(exactly = 1) { pokemonService.getPokemonInfo(pokemonName) }
            assert(response == mockResponse)
        }

    @Test
    fun `should return error when repository throws exception`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val exception = Throwable("Network error")
            coEvery { pokemonService.getPokemonInfo(pokemonName) } throws exception

            // When
            val response = try {
                useCase(pokemonName)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(response == exception)
        }
}
