package com.abelvolpi.pokemonapi.presentation.screens.details

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.DetailedPokemonResponse
import com.abelvolpi.pokemonapi.data.models.StatResponse
import com.abelvolpi.pokemonapi.data.models.StatItemResponse
import com.abelvolpi.pokemonapi.data.models.SubTypeResponse
import com.abelvolpi.pokemonapi.data.models.TypeResponse
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
