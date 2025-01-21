package com.abelvolpi.pokemonapi.presentation.screens.details

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.domain.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.models.Stat
import com.abelvolpi.pokemonapi.domain.models.StatItem
import com.abelvolpi.pokemonapi.domain.models.SubType
import com.abelvolpi.pokemonapi.domain.models.Type
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonDetailsUseCase
import com.abelvolpi.pokemonapi.presentation.UiState
import com.abelvolpi.pokemonapi.presentation.models.DetailedPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.models.StatUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private lateinit var getPokemonDetailsUseCase: GetPokemonDetailsUseCase
    private lateinit var detailsViewModel: DetailsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Before
    fun setup() {
        getPokemonDetailsUseCase = mockk()
        detailsViewModel = DetailsViewModel(getPokemonDetailsUseCase)
    }

    @Test
    fun `should fetch pokemon details successfully`() = runTest {
        // Given
        val pokemonName = "pikachu"
        val mockResponse = createMockDetailedPokemon(pokemonName)
        val expectedUiModel = createExpectedDetailedPokemonUiModel(pokemonName)
        coEvery { getPokemonDetailsUseCase.invoke(pokemonName) } returns mockResponse

        // When
        detailsViewModel.fetchPokemonDetails(pokemonName)

        // Then
        assert(detailsViewModel.pokemonDetailsState.value == UiState.Loading)
        advanceUntilIdle()
        assert(detailsViewModel.pokemonDetailsState.value == UiState.Success(expectedUiModel))
        coVerify(exactly = 1) { getPokemonDetailsUseCase.invoke(pokemonName) }
    }

    @Test
    fun `should return error when there's a failure in fetch details call`() = runTest {
        // Given
        val pokemonName = "pikachu"
        val exception = Throwable("Network error")
        coEvery { getPokemonDetailsUseCase.invoke(pokemonName) } throws exception

        // When
        detailsViewModel.fetchPokemonDetails(pokemonName)

        // Then
        assert(detailsViewModel.pokemonDetailsState.value == UiState.Loading)
        advanceUntilIdle()
        assert(detailsViewModel.pokemonDetailsState.value == UiState.Failure<Nothing>(exception))
        coVerify(exactly = 1) { getPokemonDetailsUseCase.invoke(pokemonName) }
    }

    private fun createMockDetailedPokemon(name: String) = DetailedPokemon(
        name = name,
        types = listOf(Type(SubType(typeName = "electric"))),
        weight = 6.0f,
        height = 0.4f,
        stats = listOf(
            StatItem(baseStat = 35, stat = Stat(name = "hp")),
            StatItem(baseStat = 55, stat = Stat(name = "attack"))
        )
    )

    private fun createExpectedDetailedPokemonUiModel(name: String) = DetailedPokemonUiModel(
        name = name,
        types = listOf("electric"),
        weight = 6.0f,
        height = 0.4f,
        stats = listOf(
            StatUiModel(value = 35, name = "hp"),
            StatUiModel(value = 55, name = "attack")
        )
    )
}
