package com.abelvolpi.pokemonapi.domain.usecase

import com.abelvolpi.pokemonapi.commontest.MainDispatcherRule
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

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        pokemonRepository = mockk()
        getPokemonDetailsUseCase = GetPokemonDetailsUseCase(pokemonRepository)
    }

    @Test
    fun `should return pokemon details when repository returns data`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val expectedDetailedPokemon = createDetailedPokemon(pokemonName)
            coEvery { pokemonRepository.getPokemonInfo(pokemonName) } returns expectedDetailedPokemon

            // When
            val result = getPokemonDetailsUseCase(pokemonName)

            // Then
            coVerify(exactly = 1) { pokemonRepository.getPokemonInfo(pokemonName) }
            assert(result == expectedDetailedPokemon)
        }

    @Test
    fun `should return error when repository throws exception`() =
        runTest {
            // Given
            val pokemonName = "pikachu"
            val exception = Throwable("Network error")
            coEvery { pokemonRepository.getPokemonInfo(pokemonName) } throws exception

            // When
            val result = try {
                getPokemonDetailsUseCase(pokemonName)
            } catch (error: Throwable) {
                error
            }

            // Then
            assert(result == exception)
        }

    private fun createDetailedPokemon(pokemonName: String) =
        DetailedPokemon(
            name = pokemonName,
            types = listOf(
                Type(
                    SubType(
                        typeName = "electric"
                    )
                )
            ),
            weight = 6.0f,
            height = 0.4f,
            stats = listOf(
                StatItem(
                    baseStat = 35,
                    stat = Stat(name = "hp")
                ),
                StatItem(
                    baseStat = 55,
                    stat = Stat(name = "attack")
                )
            )
        )
}
