package com.abelvolpi.pokemonapi.presentation.screens.details

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.DetailedPokemon
import com.abelvolpi.pokemonapi.data.models.Stat
import com.abelvolpi.pokemonapi.data.models.StatItem
import com.abelvolpi.pokemonapi.data.models.SubType
import com.abelvolpi.pokemonapi.data.models.Type
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonDetailsUseCase
import com.abelvolpi.pokemonapi.presentation.UiState
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

    private lateinit var mockGetPokemonDetailsUseCase: GetPokemonDetailsUseCase
    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Before
    fun setup() {
        mockGetPokemonDetailsUseCase = mockk()
        viewModel = DetailsViewModel(mockGetPokemonDetailsUseCase)
    }

    @Test
    fun `should fetch pokemon details successfully`() = runTest {
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
        coEvery { mockGetPokemonDetailsUseCase.invoke(pokemonName) } returns mockResponse

        // When
        viewModel.fetchPokemonDetails(pokemonName)

        // Then
        assert(viewModel.pokemonDetailsState.value == UiState.Loading)
        advanceUntilIdle()
        assert(viewModel.pokemonDetailsState.value == UiState.Success(mockResponse))
        coVerify(exactly = 1) { mockGetPokemonDetailsUseCase.invoke(pokemonName) }
    }

    @Test
    fun `should return error when there's a failure in fetch details call`() = runTest {
        // Given
        val pokemonName = "pikachu"
        val exception = Throwable("Network error")
        coEvery { mockGetPokemonDetailsUseCase.invoke(pokemonName) } throws exception

        // When
        viewModel.fetchPokemonDetails(pokemonName)

        // Then
        assert(viewModel.pokemonDetailsState.value == UiState.Loading)
        advanceUntilIdle()
        assert(viewModel.pokemonDetailsState.value == UiState.Failure<Nothing>(exception))
        coVerify(exactly = 1) { mockGetPokemonDetailsUseCase.invoke(pokemonName) }
    }
}
